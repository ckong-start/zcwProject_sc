package part3;


/**
 * @create 2019-12-24 8:25
 */
public class SingleLinkedList {
    private MyNode head = new MyNode(0, "", "");

    //链表添加元素
    public void add(int id, String name, String nickName){
        MyNode temp = head;
        MyNode newNode = new MyNode(id, name, nickName);
        //当temp下一个为null表示到达末尾，
        //temp下一个的id值大于传入的id，表示插到temp与下一个之间。
        while(temp.next != null){
            if (temp.next.id > id){
                break;
            }
            temp = temp.next;
        }
        //遇到相同元素情况，用新值替换旧值
        if (temp.id == id){
            temp.name = newNode.name;
            temp.nickName = newNode.nickName;
            return;
        }
        newNode.next = temp.next;
        temp.next = newNode;
    }

    //通过属性删除链表元素
    public void delete(){

    }

    //遍历链表
    public void list(){
        MyNode temp = head;
        //如果head的next为空，表示链表为空
        if (temp.next == null){
            System.out.println("{}");
            return;
        }
        while(temp.next != null){
            System.out.println(temp.next);
            temp = temp.next;
        }
    }

    //内部类
    class MyNode {
        private int id;
        private String name;
        private String nickName;
        private MyNode next;

        public MyNode() {

        }
        public MyNode(int id, String name, String nickName) {
            this.id = id;
            this.name = name;
            this.nickName = nickName;
        }

        @Override
        public String toString() {
            return "MyNode{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", nickName='" + nickName + '\'' +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public MyNode getNext() {
            return next;
        }

        public void setNext(MyNode next) {
            this.next = next;
        }
    }
}
