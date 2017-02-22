package sjtu.iiot.posiiot.Model;
import android.util.Size;

import sjtu.iiot.posiiot.GlobalValue;


public class RoutingAlgorithm {


    private static int SIZE = 11;
    private static int M = 1000;
    private int[][] edges = {
            {0,1,1,M,1,M,1,M,M,M,M},
            {1,0,M,M,M,M,M,M,M,M,M},
            {1,M,0,M,M,M,M,M,M,M,M},
            {M,M,M,0,1,M,M,M,M,M,M},
            {1,M,M,1,0,M,M,M,M,M,M},
            {M,M,M,M,M,0,M,M,M,M,1},
            {1,M,M,M,M,M,0,1,1,1,M},
            {M,M,M,M,M,M,1,0,M,1,M},
            {M,M,M,M,M,M,1,M,0,1,M},
            {M,M,M,M,M,M,1,1,M,0,1},
            {M,M,M,M,M,1,M,M,M,1,0},
    };//无向图邻接矩阵


    private int[][] path = new int[SIZE][SIZE];

    //初始化
    public RoutingAlgorithm(){
        int[][] dis = this.edges;

        for (int i = 0; i < SIZE; i = i + 1){
            for (int j = 0; j < SIZE; j = j + 1){
                this.path[i][j] = -1;
            }
        }

        for (int k = 0; k < SIZE; k = k + 1) {
            for (int i = 0; i < SIZE; i = i + 1) {
                for (int j = 0; j < SIZE; j = j + 1) {
                    if(dis[i][j] > dis[i][k] + dis[k][j]){
                        dis[i][j] = dis[i][k] + dis[k][j];
                        this.path[i][j] = k + 1;
                    }
                }
            }
        }

    }

    //输入起始点和终点位置，返回路径(如输入7,11，返回值为[7, 5][5, 3][3, 1][1, 10][10, 11])
    public int[][] getTrace(int sta, int des){
        int middlePoint = this.path[sta-1][des-1];

        if ( middlePoint == -1){
            int[][] trace = new int [1][2];
            trace[0][0] = sta;
            trace[0][1] = des;

            return trace;
        }else {
            int[][] trace1 = getTrace(sta, middlePoint);
            int[][] trace2 = getTrace(middlePoint, des);
            int length = trace1.length + trace2.length;
            int[][] trace = new int [length][2];

            for(int i = 0; i< trace1.length; i++){
                trace[i] = trace1[i];
            }

            for(int j = 0; j< trace2.length; j++){
                trace[trace1.length + j] = trace2[j];
            }
            return trace;
        }
    }

    //得到路径矩阵
    public int[][] getPathMatrix(){
        return this.path;
    }

}
