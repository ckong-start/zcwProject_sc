package part1;

import java.util.Arrays;

/**
 * @create 2019-12-21 17:51
 */
public class SparseArrayDemo {
    public static void main(String[] args) {
      /*0 0 0 0 0 0 0 0 0 0 0
		0 0 1 0 0 0 0 0 0 0 0
		0 0 0 2 0 0 0 0 0 0 0
		0 0 0 0 0 0 0 0 0 0 0
		0 0 0 0 0 0 0 0 0 0 0
		0 0 0 0 0 0 0 0 0 0 0
		0 0 0 0 0 0 0 0 0 0 0
		0 0 0 0 0 0 0 0 0 0 0
		0 0 0 0 0 0 0 0 0 0 0
		0 0 0 0 0 0 0 0 0 0 0
		0 0 0 0 0 0 0 0 0 0 0*/
        //将以上的数据用普通二维数组保存
        int[][] arr = new int[11][11];
        arr[1][2] = 1;
        arr[2][3] = 2;
        //记录数组元素不为0的个数
        int total = 0;
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[i].length; j++){
                if (arr[i][j] != 0) {

                    total++;
                }
            }
        }
        //稀疏数组的说明：该数组的行数是上述数组有效数据+1，列数是固定的3.
        //稀疏数组的第一行的数据是：第一个为上述数组的行数，第二个为上述数组的列数，第三个是有效数据的个数。
        //从第二行开始：第一个数据是第一个有效数据所在的行数，第二个数据是有效数据所在的列数，第三个为对应的值。
        //第三行同第二行，以此类推。
        int[][] sparseArr = new int[total + 1][3];
        sparseArr[0][0] = 11;
        sparseArr[0][1] = 11;
        sparseArr[0][2] = total;
        int row = 1;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] != 0){
                    sparseArr[row][0] = i;
                    sparseArr[row][1] = j;
                    sparseArr[row++][2] = arr[i][j];
                }
            }
        }
//        for (int i = 0; i < sparseArr.length; i++) {
//            for (int j = 0; j < sparseArr[i].length; j++) {
//                System.out.print(sparseArr[i][j] + " ");
//            }
//            System.out.println();
//        }

        //将稀疏数组恢复到原本的二维数组
        int[][] arr2 = new int[sparseArr[0][0]][sparseArr[0][1]];
        for (int i = 1; i < sparseArr.length; i++) {
            arr2[sparseArr[i][0]][sparseArr[i][1]] = sparseArr[i][2];
        }
        for (int i = 0; i < arr2.length; i++) {
            for (int j = 0; j < arr2[i].length; j++) {
                System.out.print(arr2[i][j] + " ");
            }
            System.out.println();
        }
    }
}
