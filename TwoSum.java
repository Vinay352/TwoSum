package Leetcode.Easy;

/**
 *
 * Author: Vinay Jain
 * Contact: vinay.j3097@gmail.com
 * Question: Two Sum
 * Link: https://leetcode.com/problems/two-sum/
 *
 * Approach 1: inbuilt function
 *
 * Approach 2: Merge Sort + 2 pointer approach
 *
 * Approach 3: Use n array of objects to store values and indices of elements + inbuilt sort + 2 pointer approach
 *
 * Approach 4: for every element, do a binary search for (target - element) in array
 *
 */

import java.util.Arrays;

public class TwoSum {

    /**
     *
     * class for approach 3
     * this class stores array values with their indices in the array
     *
     */
    static class ValueIndexNode implements Comparable<ValueIndexNode>{
        int val;
        int index;

        public ValueIndexNode(){

        }

        public ValueIndexNode(int val, int index){
            this.val = val;
            this.index = index;
        }

        @Override
        public int compareTo(ValueIndexNode o) {
            return this.val - o.val;
        }
    }

    /**
     * main method
     * @param args
     */
    public static void main(String[] args) {
        // int[] nums = {15, 11, 2 ,7};

        // int[] nums = {2 ,7, 15, 11};

        // int[] nums = {3, 2, 4};

        // int[] nums = {3, 3};

        int[] nums = {2,5,5,11};

        //O(n^2) solution
        // bruteForceSolution(nums, 9);

        int[] ans = optimizedToNlogN(nums, 10);

        printArray(ans);
    }

    /**
     * Solution with NlogN approach.
     *
     * @param nums
     * @param target
     * @return array with the 2 required indices
     */
    private static int[] optimizedToNlogN(int[] nums, int target) {

        // Approach 1
        // can check if sum exist but can't check which indices - loses track of it
        // return approach_1(nums, target);


        // Approach 2
        // time limit exceeded but correct
        // return aprroach_2(nums, target);

        // Approach 3
        // accepted
        // return approach_3(nums, target);

        // Approach 4
        // accepted
        return approach_4(nums, target);
    }

    /**
     * Copy the original array int o a copy array. For every element in copy array, do a
     * binary search for (target - copyArrayElement) and if it is found, search for that element
     * in the original array to get the original index of that element.
     *
     * @param nums input array
     * @param target required sum
     * @return array of required indices
     */
    private static int[] approach_4(int[] nums, int target) {

        // this array is copied into another array.
        // doing this is IMPORTANT IN THIS MANNER otherwise when you do Arrays.sort() on the copy array,
        // the original array elements also get modified and thus, it becomes difficult to keep track of
        // actual indices of the array elements.
        int[] copyNums = new int[nums.length];
        for(int i = 0; i < nums.length; i++){
            copyNums[i] = nums[i];
        }


        int[] ans = new int[2];

        Arrays.sort(copyNums);

        // printArray(copyNums);

        int i = 0;
        int j = 0;

        for(i = 0; i < copyNums.length; i++){
            j = binarySearch(copyNums, i+1, nums.length - 1, target - copyNums[i]);

            System.out.println("j = " + j);

            if(j != -1){
                break;
            }
        }

        ans[0] = findInActualArray(nums, copyNums[i], -1);
        ans[1] = findInActualArray(nums, copyNums[j], ans[0]);

        return ans;

    }

    /**
     * Locate "search" element in original array and return the index where it is found.
     * The index to be returned should not be the same as "shouldNotBeSame".
     *
     * @param nums
     * @param search
     * @param shouldNotBeSame
     * @return
     */
    private static int findInActualArray(int[] nums, int search, int shouldNotBeSame) {
        int found = 0;
        for(int i = 0; i < nums.length; i++){
            if(nums[i] == search && shouldNotBeSame != i){
                found = i;
            }
        }
        return found;
    }

    /**
     * Code to perform binary search on array
     *
     * @param nums
     * @param left
     * @param right
     * @param search
     * @return
     */
    private static int binarySearch(int[] nums, int left, int right, int search) {
        if(left > right){
            return -1;
        }

        int mid = left + (right - left)/2;
        if(search == nums[mid]){ // if match found
            return mid;
        }else if(search > nums[mid]){ // search in right half
            return binarySearch(nums, mid + 1, right, search);
        }else{ // search in left half
            return binarySearch(nums, left, mid - 1, search);
        }
    }

    /**
     * Create an array of objects where each object can store value of the array element and corresponding index.
     * Then sort these objects based on their values and then do a linear traversal with 2 pointers - 1 from left
     * and 1 from right. Increase the left pointer if sum of elements at left and right pointer < target,
     * if > target => decrease right pointer
     * if == target, return the indices.
     *
     * @param nums
     * @param target
     * @return
     */
    private static int[] approach_3(int[] nums, int target) {

        int[] index = createIndexArray(nums);

        ValueIndexNode[] nodes = new ValueIndexNode[nums.length];

        for(int i = 0; i < nums.length; i++){
            nodes[i] = new ValueIndexNode(nums[i], index[i]);
        }

        Arrays.sort(nodes);

        // printNodes(nodes);

        int[] ans = new int[2];

        int left = 0;
        int right = nums.length - 1;
        while( left < right ){
            if( nodes[left].val + nodes[right].val > target ){
                right--;
            }else if( nodes[left].val + nodes[right].val < target ){
                left++;
            }else{
                // found required indices

//                 System.out.println("[" + index[left] + ", " + index[right] + "]");
//                 return ;

                ans[0] = nodes[left].index;
                ans[1] = nodes[right].index;
                break;
            }
        }
        return ans;

    }

    /**
     * A user defined fucntion to print the objects in approach 3.
     * @param nodes
     */
    private static void printNodes(ValueIndexNode[] nodes){

        for(int i = 0; i < nodes.length; i++){
            System.out.print(nodes[i].val + " " + ", " + nodes[i].index);
            System.out.println();
        }

    }

    /**
     * The idea is to keep a separate array for indices - an array which stores index values of elements
     * from the corresponding indices in original array. Use merge sort to sort the main array and change
     * the index array (not sort) as per the corresponding swaps in original array. This way you store the
     * original indices of the array in the index array while simultaneously sorting the main array.
     *
     * After sorting, use the 2 pointer approach.
     *
     * @param nums
     * @param target
     * @return
     */
    private static int[] aprroach_2(int[] nums, int target) {
        int[] index = createIndexArray(nums); // store indices of elements in actual array
        // merge sort the original array and change the positions of index array elements
        // as you change the original array elements. Note that actual array is sorted whereas
        // index array is just moved and not sorted.
        mergeSort(nums, index, 0 , nums.length - 1);


        int[] ans = new int[2];

        int left = 0;
        int right = nums.length - 1;
        while( left < right ){
            if( nums[left] + nums[right] > target ){
                right--;
            }else if( nums[left] + nums[right] < target ){
                left++;
            }else{
                // found required indices

//                 System.out.println("[" + index[left] + ", " + index[right] + "]");
//                 return ;

                ans[0] = index[left];
                ans[1] = index[right];
                break;
            }
        }
        return ans;
    }

    // with this approach - using inbuilt sorting, we lose the actual indices of the elements
    private static int[] approach_1(int[] nums, int target) {

        Arrays.sort(nums);

        int[] ans = new int[2];

        int left = 0;
        int right = nums.length - 1;
        while( left < right ){
            if( nums[left] + nums[right] > target ){
                right--;
            }else if( nums[left] + nums[right] < target ){
                left++;
            }else{
                // found required indices

//                 System.out.println("[" + index[left] + ", " + index[right] + "]");
//                 return ;

                ans[0] = left;
                ans[1] = right;
            }
        }
        return ans;
    }

    private static void printArray(int[] nums) {

        for(int i = 0; i < nums.length; i++){
            System.out.print(nums[i] + " ");
        }
        System.out.println();

    }

    /**
     * function to implement merge sort
     *
     * @param nums
     * @param index
     * @param leftIndex
     * @param rightIndex
     */
    private static void mergeSort(int[] nums, int[] index, int leftIndex, int rightIndex) {

        if(rightIndex - leftIndex + 1 < 2){
            return ;
        }

        int mid = leftIndex + (rightIndex - leftIndex)/2;

        mergeSort(nums, index, leftIndex, mid);
        mergeSort(nums, index, mid + 1, rightIndex);

        int[] leftArray = new int[mid - leftIndex + 1];
        for(int i = leftIndex; i < mid + 1; i++){
            leftArray[i - leftIndex] = nums[i];
        }

        int[] rightArray = new int[rightIndex - mid];
        for(int i = mid + 1; i <= rightIndex; i++){
            rightArray[i - mid - 1] = nums[i];
        }

        int[] leftIndexArray = new int[mid - leftIndex + 1];
        for(int i = leftIndex; i < mid + 1; i++){
            leftIndexArray[i - leftIndex] = index[i];
        }

        int[] rightIndexArray = new int[rightIndex - mid];
        for(int i = mid + 1; i <= rightIndex; i++){
            rightIndexArray[i - mid - 1] = index[i];
        }


        merge(nums, index, leftArray, rightArray, leftIndex, mid, rightIndex, leftIndexArray, rightIndexArray);

    }

    /**
     * function to implement merge part of the merge sort
     *
     * @param nums
     * @param index
     * @param leftArray
     * @param rightArray
     * @param leftIndex
     * @param mid
     * @param rightIndex
     * @param leftIndexArray
     * @param rightIndexArray
     */
    private static void merge(int[] nums, int[] index, int[] leftArray, int[] rightArray, int leftIndex, int mid, int rightIndex, int[] leftIndexArray, int[] rightIndexArray) {
        int n = nums.length;
        int leftArrayLength = leftArray.length;
        int rightArrayLength = rightArray.length;

        int i = 0;
        int j = 0;
        int k = leftIndex;
        while(i < leftArrayLength && j < rightArrayLength){
            if(leftArray[i] <= rightArray[j]){
                nums[k] = leftArray[i];
                index[k] = leftIndexArray[i];
                i++;
                k++;
            }
            else{
                nums[k] = rightArray[j];
                index[k] = rightIndexArray[j];
                j++;
                k++;
            }
        }

        while(i < leftArrayLength){
            nums[k] = leftArray[i];
            index[k] = leftIndexArray[i];
            i++;
            k++;
        }

        while(j < rightArrayLength){
            nums[k] = rightArray[j];
            index[k] = rightIndexArray[j];
            j++;
            k++;
        }
    }

    /**
     * A user defined function for creating an array for storing index values of every input element.
     *
     * @param nums
     * @return
     */
    private static int[] createIndexArray(int[] nums) {
        int n = nums.length;
        int[] index = new int[n];

        for(int i = 0; i < n; i++){
            index[i] = i;
        }

        return index;

    }

    /**
     * for every element in the array, check whether ther exist another element
     * in the array with sum == target
     *
     * @param nums input array
     * @param target the target sum of 2 array elements
     */
    private static void bruteForceSolution(int[] nums, int target) {
        for(int i = 0; i < nums.length - 1; i++){
            for(int j = i + 1; j < nums.length; j++){
                if(nums[i] + nums[j] == target){
                    // found the answer
                    System.out.println("[" + i + ", " + j + "]");
                    return ;
                }
            }
        }
        System.out.println("No such elements");
    }
}
