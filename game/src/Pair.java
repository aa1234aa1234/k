public class Pair<A,B> {
    private A first;
    private B second;

    public Pair(A first, B second){
        this.first = first;
        this.second = second;
    }

    public static <A,B> Pair<A,B> of(A a, B b) {
        return new Pair<A,B>(a,b);
    }

    public void setFirt(A a) {
        first = a;
    }

    public void setSecond(B a) {
        second = a;
    }

    public A getFirst() { return first; }

    public B getSecond() { return second; }
}
