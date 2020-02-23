package part3;

/**
 * @create 2019-12-24 8:25
 */
public class SingleLinkedListDemo {
    public static void main(String[] args) {
        SingleLinkedList sll = new SingleLinkedList();
        sll.list();
        sll.add(1, "one", "1");
        sll.add(5, "five", "5");
        sll.add(2, "two", "2");
        sll.add(3, "three", "3");
        sll.add(4, "four", "4");
        sll.list();
        System.out.println();
        sll.add(5, "five", "55");
        sll.list();
    }
}
