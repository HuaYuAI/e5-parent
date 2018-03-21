/**
 * Created by HuaYu on 2018/3/14.
 */
public class UserTest {
    public static int getMiddle(int [] sortArray,int low,int high){
        int key =sortArray[low];
        while (low<high){
            while (low<high && sortArray[high]>=key){
                high--;
            }
            sortArray[low]=sortArray[high];
            while (low<high && sortArray[low]<=key){
                low++;
            }
            sortArray[high]=sortArray[low];
        }
        sortArray[low]=key;
        return low;
    }
    public static void quicksort(int[] sortArray,int low,int high){
        if(low<high){
            int middle=getMiddle(sortArray,low,high);
            quicksort(sortArray,low,middle-1);
            quicksort(sortArray,middle+1,high);
        }
        System.out.println("ok");
        sprint(sortArray);
    }

    private static void sprint(int[] arrays) {
        for (int i=0;i<arrays.length;i++){
            System.out.println(arrays[i]);
        }
    }
    public static void main(String[] args){
        int [] array={12,56,44,88,99,77,11,5,45,65,35,26,51};
        quicksort(array,0,array.length-1);
    }
}
