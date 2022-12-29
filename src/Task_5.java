import mpi.MPI;
import mpi.Request;
import mpi.Status;

import java.util.*;

public class Task_5 {

    public static void main(String[] args){

        // ������������� ����� �����������������
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int tag = 100;

        Request[] req = new Request[size-1];
        Status[] sta = new Status[size-1];



        // 0 ���������
        if(rank == 0){
            // �������
            int n_1 = 10;
            int n_2 = 10;
            int n_3 = 10;


            // ������ ������� A
            int[] a = new int[n_1 * n_2];

            // ������ ������� B
            int[] b = new int[n_2 * n_3];

            // ������ ������� C
            int[] c = new int[n_1 * n_3];

            // ��������� ������� A � B
            for(int i = 0; i < n_1 * n_2; i++){
                a[i] = 1;
            }
            for(int i = 0; i < n_3 * n_2; i++){
                b[i] = 1;
            }


            // ��������� �������� ���� �����������
            for(int i = 1; i < size; i++){
                int n = n_1 * n_2;
                int next = n_1 / size;
                for(int g = 0; g < next; g++){
                    // �������� ����� ��� ��������
                    int[] send = new int[n_2 * next];

                    // ��������, ��� ���������
                    for(int j = 0; j < n_2 * next; j++){

                    }

                    req[rank]=MPI.COMM_WORLD.Isend(number, 0, 1, MPI.INT, myrank+1, tag);
                    sta[rank]=req[rank].Wait();
                }
                if (req[myrank].Is_null())  System.out.println(rank + " send to "+ (rank_next) + " complete");
            }

            number[0] = myrank;
            MPI.COMM_WORLD.Recv(number)

        }

        MPI.Finalize();
    }
}
