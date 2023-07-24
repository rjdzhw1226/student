package com.student.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class eightQueen {

    public static void main(String[] args) {
        int boardSizeRow = 4;//棋盘行
        int boardSizeCol = 4;//棋盘列
        int count = 0;//数量
        List<List<Integer>> solutions = eightQueen(boardSizeRow, boardSizeCol);
        for (List<Integer> solution : solutions) {
            System.out.println(solution);
            count++;
        }
        System.out.println(count);
    }

    public static List<List<Integer>> eightQueen(int col, int boardSize){
        List<List<Integer>> solutions = new ArrayList<>();
        if(col == 1){
            for (int i = 0; i <= boardSize; i++) {
                List<Integer> init = new ArrayList<>();
                init.add(i);
                solutions.add(init);
            }
            return solutions;
        }
        List<List<Integer>> restQueenSolutions = eightQueen(col - 1 , boardSize);
        return restQueenSolutions.stream()
                .map(restQueen -> tryPlay(restQueen,boardSize))
                .flatMap(tryPlaySolutions -> tryPlaySolutions.stream())
                .filter(t -> checkTryPlay(t))
                .collect(Collectors.toList());
    }

    private static boolean checkTryPlay(List<Integer> queens) {
        int row = queens.size();
        int col = queens.get(row - 1);

        for (int i = 0; i < row - 1; i++) {
            int rowDiff = Math.abs(row - i - 1);
            int colDiff = Math.abs(col - queens.get(i));
            if(rowDiff == colDiff || col == queens.get(i)){
                return false;
            }
        }

        return true;
    }

    private static List<List<Integer>> tryPlay(List<Integer> restQueen, int boardSize) {
        List<List<Integer>> trySolutionds = new ArrayList<>();
        for (int i = 0; i <= boardSize; i++) {
            List<Integer> temp = new ArrayList<>(restQueen);
            temp.add(i);
            trySolutionds.add(temp);
        }
        return trySolutionds;
    }

}
