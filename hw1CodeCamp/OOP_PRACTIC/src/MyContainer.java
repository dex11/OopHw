public interface MyContainer{
    public int size();
    public int get(int index);
    default public int getMax(){
        if(size() == 0) return 0;
        int ret = get(0);
        for(int i = 1; i < size(); i++){
            ret = Math.max(reg, get(i));
        }
        return ret;
    }
}
