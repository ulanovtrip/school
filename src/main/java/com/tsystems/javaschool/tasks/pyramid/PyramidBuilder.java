package com.tsystems.javaschool.tasks.pyramid;
import java.util.*;
import java.util.stream.Collectors;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        boolean flag;//Флаг для возможности/невозможности построения пирамиды
        int[][] matrix;//Получившаяся матрица
        int size = inputNumbers.size();//Проверяем размер полученного массива
        int maxValue = Integer.MAX_VALUE - 1;

        if(inputNumbers.contains(null)) {
            throw new CannotBuildPyramidException();
        }
        if(size == maxValue) {
            throw new CannotBuildPyramidException();
        }
        //Проверим, является ли данное число треугольным
        int count = 0;
        int rows = 1;
        int columns = 1;

        while(count < size) {
            count += rows;
            rows++;
            columns += 2;
        }
        rows -= 1;//Актуальное число строк
        columns -= 2;//Актуальное число столбцов

        flag = size == count;//Если возможно построить матрицу

        if(flag){
            List<Integer> sorted = inputNumbers.stream().sorted().collect(Collectors.toList());

            /******* ЗАПОЛНЯЕМ МАТРИЦУ НУЛЯМИ *******/
            matrix = new int[rows][columns];//Задаем размерность матрице
            for (int[] row : matrix) {
                Arrays.fill(row, 0);
            }
            /******* СТРОИМ ТРЕУГОЛЬНИК *******/
            int center = (columns / 2);//Находим центральную точку матрицы
            count = 1; // сколько чисел будет в строке
            int arrIdx = 0; // индекс массива

            for (int i = 0, offset = 0; i < rows; i++, offset++, count++) {
                int start = center - offset;
                for (int j = 0; j < count * 2; j += 2, arrIdx++) {
                    matrix[i][start + j] = sorted.get(arrIdx);
                }
            }
            /******** ВЫВОДИМ МАТРИЦУ НА ЭКРАН *******/
            for(int [] a: matrix)//выводим матрицу на экран
            {
                for(int b: a)
                    System.out.print(b+" ");
                System.out.println();
            }
        }
        //Выбрасываем исключение
        else{
            throw new CannotBuildPyramidException();
        }
        return matrix;
    }
}
