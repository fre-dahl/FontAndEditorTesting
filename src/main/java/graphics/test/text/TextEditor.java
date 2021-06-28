package graphics.test.text;

import java.util.Iterator;
import java.util.function.Consumer;

import static graphics.test.text.TextEditor.CharEntry;
import static java.nio.charset.StandardCharsets.US_ASCII;

public class TextEditor implements Iterable<CharEntry> {

    private final CharEntry sentinel;
    private CharEntry firstEntry;
    private CharEntry pointer;
    private int count;

    public TextEditor() {
        sentinel = new CharEntry((byte)0x20);
        pointer = sentinel;
        count = 0;
    }

    public void commandType(byte charCode) {

        CharEntry newNode;

        if (isEmpty()) {

            newNode = new CharEntry(sentinel,charCode,null);

            firstEntry = newNode;

            sentinel.setNext(firstEntry);
        }
        else {

            newNode = new CharEntry(pointer,charCode,pointer.next());

            pointer.setNext(newNode);
        }
        pointer = newNode;

        count++;
    }

    public boolean commandType(char c) {

        if (c <= Byte.MAX_VALUE) {

            commandType((byte) c);

            return true;
        }
        return false;
    }

    public void commandInsert(byte[] us_ascii) {

        for (byte b : us_ascii) commandType(b);

    }

    public boolean commandInsert(CharSequence text) {

        byte[] us_ascii = text.toString().getBytes(US_ASCII);

        if (us_ascii.length == text.length()) {

            commandInsert(us_ascii);

            return true;
        }
        return false;
    }

    public void commandSpace() {

        commandType((byte)0x0F);
    }

    public void commandNewLine() {

        commandType((byte)0x0A);
    }

    public void commandTab() {

        commandType((byte)0x09);
    }

    public void commandDelete() {

        if (isEmpty() || onSentinel()) return;

        if (onFirstEntry()) {

            if (pointer.hasNext()) {

                pointer.next().setPrev(pointer.prev());
            }
            pointer.prev().setNext(pointer.next());

            pointer = firstEntry = pointer.next();
        }
        else {

            if (pointer.hasNext()) {

                pointer.next().setPrev(pointer.prev());
            }
            pointer.prev().setNext(pointer.next());

            pointer = pointer.next();
        }
        count--;
    }

    public void moveForward() {

        if (pointer.hasNext()) {

            pointer = pointer.next();
        }
    }

    public void moveBackward() {

        if (pointer.hasPrev()) {

            pointer = pointer.prev();
        }
    }

    public void moveToStart() {

        pointer = sentinel;
    }

    public void moveToEnd() {

        while (pointer.hasNext()) {

            pointer = pointer.next();
        }
    }

    public byte[] getBytes() {

        if (isEmpty()) return null;

        byte[] bytes = new byte[count];

        CharEntry entry = firstEntry;

        int i = 0;

        do {
            bytes[i++] = entry.code();
            entry = entry.next();
        }
        while (entry != null);

        return bytes;
    }

    public boolean isEmpty() {

        return count == 0;
    }

    public int length() {

        return count;
    }

    public boolean isPointer(CharEntry entry) {

        return pointer.equals(entry);
    }

    private boolean onSentinel() {

        return !pointer.hasPrev();
    }

    private boolean onFirstEntry() {

        return pointer.equals(firstEntry);
    }

    private boolean atEnd() {

        return !pointer.hasNext();
    }

    @Override
    public String toString() {

        return isEmpty() ? "" : new String(getBytes(),US_ASCII);
    }

    @Override
    public Iterator<CharEntry> iterator() {

        return new Iterator<>() {

            private final CharEntry entry = TextEditor.this.firstEntry;

            @Override
            public boolean hasNext() {

                return entry.hasNext();
            }

            @Override
            public CharEntry next() {

                return entry.next();
            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public void forEach(Consumer<? super CharEntry> action) {

        Iterable.super.forEach(action);
    }

    protected static class CharEntry {

        CharEntry next;
        CharEntry prev;
        byte charCode;

        public CharEntry(byte charCode) {
            this(null,charCode,null);
        }

        public CharEntry(CharEntry prev, byte charCode, CharEntry next) {
            this.prev = prev;
            this.next = next;
            this.charCode = charCode;
        }

        CharEntry next() { return next; }

        CharEntry prev() { return prev; }

        void setNext (CharEntry entry) { next = entry; }

        void setPrev(CharEntry entry) { prev = entry; }

        byte code() { return charCode; }

        void setCode(byte charCode) { this.charCode = charCode; }

        boolean hasNext() { return next != null; }

        boolean hasPrev() { return prev != null; }
    }
}
