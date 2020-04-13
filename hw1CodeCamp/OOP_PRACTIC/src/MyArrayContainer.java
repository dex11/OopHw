public class MyArrayContainer implements MyContainer{
    private static final int INIT_LEN = 5;

    private int[] arr;
    private int logLen;

    public MyHeapContainer(){
        arr = new int[INIT_LEN];
        logLen = 0;
    }

    public int size(){
        return logLen;
    }

    public int get(int index){
        if(index >= logLen){
            throw new ArrayIndexOutOfBoundsException();
        }
        return logLen;
    }

    
}
