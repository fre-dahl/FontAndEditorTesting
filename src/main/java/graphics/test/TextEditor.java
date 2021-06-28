package graphics.test;


import org.davidmoten.text.utils.WordWrap;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TextEditor {

    // https://www.cs.unm.edu/~crowley/papers/sds.pdf

    // todo: replace internal getter method calls with the actual field value?
    //  fewer instruction calls? no clue.

    // todo: # redo structure - priority low
    // This Works fine, but i will have to rethink the data structure when i have the time.
    // The double-linked structure is just fine, that's not the problem.
    // It's the text wrapping that mess up the pointers position
    // i want to have more control, i want line nr. word count, what line / word is the pointer on etc.

    private static final Charset US_ASCII = StandardCharsets.US_ASCII;

    private static final int FONT_WIDTH_NULL = -1;

    private final Node<Character> sentinel;
    private Node<Character> firstChar;
    private Node<Character> pointer;
    private int internalCount = 0; // list-entries

    private final int maxChars;
    private int fontW;
    private int containerW;
    private boolean dirty;
    private boolean useWrap;

    private byte[] data; // bytebuffer?

    public TextEditor() {
        this(Integer.MAX_VALUE);
    }

    public TextEditor(int maxChars) {
        this(maxChars, FONT_WIDTH_NULL,Integer.MAX_VALUE,false);
    }

    public TextEditor(int maxChars, int fontW, int containerW, boolean useWrap) {

        sentinel = new Node<>(' ');
        pointer = sentinel;

        this.fontW = fontW;
        this.containerW = containerW;
        this.maxChars = Math.max(maxChars,1);
        this.useWrap = useWrap;
    }

    public void commandInsert(CharSequence text) {

        int len = text.length();

        for (int i = 0; i < len; i++) {

            if (!commandType(text.charAt(i))) break;
        }
    }

    public boolean commandType(Character c) {

        if (hasAvailableSpace()) {

            Node<Character> newNode;

            if (isEmpty()) {

                newNode = new Node<>(sentinel,c,null);

                firstChar = newNode;

                sentinel.setNext(firstChar);
            }
            else {

                newNode = new Node<>(pointer,c,pointer.next());

                pointer.setNext(newNode);
            }
            pointer = newNode;

            internalCount++;

            return dirty = true;
        }
        return false;
    }

    public void commandSpace() {
        commandType(' ');
    }

    public void commandNewLine() {
        commandType('\n');
    }

    public void commandTab() {
        commandType('\t');
    }

    public void commandDelete() {

        if (isEmpty() || onSentinel()) return;

        if (onFirstChar()) {

            if (pointer.hasNext()) {

                pointer.next().setPrev(pointer.prev());
            }
            pointer.prev().setNext(pointer.next());

            pointer = firstChar = pointer.next();
        }
        else {

            if (pointer.hasNext()) {

                pointer.next().setPrev(pointer.prev());
            }
            pointer.prev().setNext(pointer.next());

            pointer = pointer.next();
        }
        internalCount--;

        dirty = true;
    }

    public void commandClear() {

        if (!isEmpty()) {

            sentinel.setNext(null);
            pointer = firstChar = null;
            internalCount = 0;
            data = null;
        }
    }

    public void moveForward() {

        if (pointer.hasNext()) {

            pointer = pointer.next();

            System.out.println(pointer.element);
        }
    }

    public void moveBackward() {

        if (pointer.hasPrev()) {

            pointer = pointer.prev();

            System.out.println(pointer.element);
        }
    }

    public void moveDown() {

        if (isEmpty()) return;

        int counter = lineLength();

        while (pointer.hasNext()) {

            pointer = pointer.next();

            if (--counter == 0) break;
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

    public void setDimensions(int fontW, int containerW) {

        this.fontW = fontW;
        this.containerW = containerW;

        if (isUsingWordWrap()) dirty = true;
    }

    public boolean isEmpty() {

        return internalCount == 0;
    }

    public boolean isUsingWordWrap() {

        return useWrap;
    }

    public boolean hasAvailableSpace() {

        return (maxChars - internalCount) > 0;
    }

    public int internalCount() {

        return internalCount;
    }

    public int lineLength() {
        if (fontW == FONT_WIDTH_NULL) {
            return Integer.MAX_VALUE;
        }
        return containerW / fontW;
    }

    public int size() {

        return data.length;
    }

    @Override
    public String toString() {

        return isEmpty() ? " " : new String(getBytes());

    }

    public void toggleWrappingOn() {

        useWrap = true;

        if (!isEmpty()) dirty = true;
    }

    public void toggleWrappingOf() {

        useWrap = false;

        if (!isEmpty()) dirty = true;
    }

    public void print() {

        System.out.println(this);
    }

    public byte[] getBytes() {

        if (isEmpty()) return null;

        if (dirty) refresh();

        return data;
    }

    // todo # Double Conversion

    private void refresh() {

        byte[] array = new byte[internalCount()];

        Node<Character> node = firstChar;

        int i = 0;

        do {
            array[i++] = (byte) node.element().charValue();
            node = node.next();
        }
        while (node != null);

        if (isUsingWordWrap()) { // Annoying double conversions.. It's acceptable.

            String s = new String(array,US_ASCII);
            String q;

            System.out.println(s);
            System.out.println();

            data = WordWrap.from(s).maxWidth(lineLength()).wrap().getBytes(US_ASCII);
            q = new String(data,US_ASCII);
            System.out.println(q);
        }
        else {

            data = array;
        }
        dirty = false;
    }

    private boolean atEnd() {

        return !pointer.hasNext();
    }

    private boolean onSentinel() {

        return !pointer.hasPrev();
    }

    private boolean onFirstChar() {

        return pointer.equals(firstChar);
    }

    private static class Node<E> {

        Node<E> next;
        Node<E> prev;
        E element;

        public Node(E elem) {
            this.prev = null;
            this.next = null;
            element = elem;
        }

        public Node(Node<E> prev, E elem, Node<E> next) {
            this.prev = prev;
            this.next = next;
            element = elem;
        }

        Node<E> next() { return next; }

        Node<E> prev() { return prev; }

        void setNext (Node<E> dnode) { next = dnode; }

        void setPrev(Node<E> dnode) { prev = dnode; }

        E element() { return element; }

        void setElement (E elem) { element = elem; }

        boolean hasNext() { return next != null; }

        boolean hasPrev() { return prev != null; }

    }
}
