import mpi.MPI;
import mpi.Request;
import mpi.Status;

import java.util.*;

public class Task_3 {
    public static void main(String[] args){

        // ������������� ����� �����������������
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int tag = 100;
        double[] num = new double[1];


        Request[] req = new Request[size];
        Status[] status = new Status[size];

        if (rank % 2 == 0 & rank > 2) {
            num[0] = Math.round((Math.random()*100));

            req[rank] =  MPI.COMM_WORLD.Isend(num, 0, 1, MPI.DOUBLE, 2, tag);
            status[rank]=req[rank].Wait();
            if (req[rank].Is_null())  System.out.println("This is process = "+ rank+ " " + "Sending to : " + 2 + " success!");
        }
        else if (rank % 2 == 1 & rank > 2) {
            num[0] = Math.round((Math.random()*100));

            req[rank] =  MPI.COMM_WORLD.Isend(num, 0, 1, MPI.DOUBLE, 1, tag);
            status[rank]=req[rank].Wait();
            if (req[rank].Is_null())  System.out.println("This is process = "+ rank+ " " + "Sending to : " + 1 + " success!");
        }
        else if (rank == 1) {


            double[] req_send = new double[size/2 - 1];
            int k = 0;

            // ��������� �������� � �������� �����������
            for (int i = 3; i < size; i += 2){
                MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, i, tag);
                req_send[k] = num[0];
                k++;
            }

            Arrays.sort(req_send);// ����������
            System.out.println("This is process = "+ rank+ " " + "Sort : " + Arrays.toString(req_send) + " success!");

            for (double v : req_send) {
                num[0] = v;
                req[rank] = MPI.COMM_WORLD.Isend(num, 0, 1, MPI.DOUBLE, 0, tag);
                status[rank] = req[rank].Wait();
                if (req[rank].Is_null())
                    System.out.println("This is process = " + rank + " " + "Sending : " + v + " success!");
            }
        }
        else if (rank == 2) {
            double[] req_send = new double[size/2 - 1];
            int k = 0;

            // ��������� �������� � ������ ���������
            for (int i = 4; i < size; i += 2){
                MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, i, tag);
                req_send[k] = num[0];
                k++;
            }

            Arrays.sort(req_send);// ����������
            System.out.println("This is process = "+ rank+ " " + "Sort : " + Arrays.toString(req_send) + " success!");

            for (double v : req_send) {
                num[0] = v;
                req[rank] = MPI.COMM_WORLD.Isend(num, 0, 1, MPI.DOUBLE, 0, tag);
                status[rank] = req[rank].Wait();
                if (req[rank].Is_null())
                    System.out.println("This is process = " + rank + " " + "Sending : " + v + " success!");
            }

        }

        else if (rank == 0) {

            //

            double[] req_send = new double[size-3];

            // ���������� ��� �������� ��������
            double first,second;

            // ��������
            int k = 0;
            int first_k = 0,second_k = 0;


            // �������� ������ �������� �� ���� �������������
            MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 1, tag);
            first = num[0];
            MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 2, tag);
            second = num[0];


            // ���� ���� �� ������� ��������� �������
            while(k < req_send.length){
                if(first > second & second_k < req_send.length / 2){
                    req_send[k] = second; // ����������� �������
                    second_k++; // ��������� ������� ��� ������� ����������
                    k++; // ��������� ����� ���������� ����������� ���������

                    // ��������� ��������, ���� ����, ��� ��������� - ���������
                    if(second_k < req_send.length / 2) MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 2, tag); // �������� ��������� ��������
                    second = num[0];
                }
                else if (first <= second & first_k < req_send.length / 2 ) {
                    req_send[k] = first; // ����������� �������
                    first_k++; // ��������� ������� ��� ������� ����������
                    k++; // ��������� ����� ���������� ����������� ���������

                    // ��������� ��������, ���� ����, ��� ��������� - ���������
                    if(first_k < req_send.length / 2) MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 1, tag); // �������� ��������� ��������
                    first = num[0];
                }
                // ���� ����������� �������� �� ������
                else if(second_k >= req_send.length / 2){
                    req_send[k] = first; // ����������� �������
                    first_k++; // ��������� ������� ��� ������� ����������
                    k++; // ��������� ����� ���������� ����������� ���������

                    // ��������� ��������, ���� ����, ��� ��������� - ���������
                    if(first_k < req_send.length / 2) MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 1, tag); // �������� ��������� ��������
                    first = num[0];
                }
                // ���� ����������� �������� � ������
                else if(first_k >= req_send.length / 2){
                    req_send[k] = second; // ����������� �������
                    second_k++; // ��������� ������� ��� ������� ����������
                    k++; // ��������� ����� ���������� ����������� ���������

                    // ��������� ��������, ���� ����, ��� ��������� - ���������
                    if(second_k < req_send.length / 2) MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 2, tag); // �������� ��������� ��������
                    second = num[0];
                }

            }
            System.out.println("Result = " + Arrays.toString(req_send));

        }

        MPI.Finalize();
    }
}
