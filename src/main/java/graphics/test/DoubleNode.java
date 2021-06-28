package graphics.test;

public class DoubleNode<E> {

    DoubleNode<E> next;
    DoubleNode<E> prev;
    E element;

    public DoubleNode(E elem) {
        this.prev = null;
        this.next = null;
        element = elem;
    }

    public DoubleNode(DoubleNode<E> prev, E elem, DoubleNode<E> next) {
        this.prev = prev;
        this.next = next;
        element = elem;
    }

    DoubleNode<E> next() { return next; }

    DoubleNode<E> prev() { return prev; }

    void setNext (DoubleNode<E> dnode) { next = dnode; }

    void setPrev(DoubleNode<E> dnode) { prev = dnode; }

    E element() { return element; }

    void setElement (E elem) { element = elem; }

    boolean hasNext() { return next != null; }

    boolean hasPrev() { return prev != null; }

}
