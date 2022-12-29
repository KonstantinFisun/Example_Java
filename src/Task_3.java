import mpi.MPI;
import mpi.Request;
import mpi.Status;

import java.util.*;

public class Task_3 {
    public static void main(String[] args){

        // Инициализация блока распараллеливания
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

            // Принимаем элементы с нечетких процессоров
            for (int i = 3; i < size; i += 2){
                MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, i, tag);
                req_send[k] = num[0];
                k++;
            }

            Arrays.sort(req_send);// Сортировка
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

            // Принимаем элементы с четных процессов
            for (int i = 4; i < size; i += 2){
                MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, i, tag);
                req_send[k] = num[0];
                k++;
            }

            Arrays.sort(req_send);// Сортировка
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

            // Переменные для хранения значений
            double first,second;

            // Счетчики
            int k = 0;
            int first_k = 0,second_k = 0;


            // Получаем первые элементы из двух сортированных
            MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 1, tag);
            first = num[0];
            MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 2, tag);
            second = num[0];


            // Идем пока не получим последний элемент
            while(k < req_send.length){
                if(first > second & second_k < req_send.length / 2){
                    req_send[k] = second; // Присваиваем меньший
                    second_k++; // Обновляем счётчик для второго процессора
                    k++; // Обновляем общее количество добавленных элементов

                    // Выполняем проверку, если есть, что принимать - принимаем
                    if(second_k < req_send.length / 2) MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 2, tag); // Получаем следующее значение
                    second = num[0];
                }
                else if (first <= second & first_k < req_send.length / 2 ) {
                    req_send[k] = first; // Присваиваем меньший
                    first_k++; // Обновляем счётчик для первого процессора
                    k++; // Обновляем общее количество добавленных элементов

                    // Выполняем проверку, если есть, что принимать - принимаем
                    if(first_k < req_send.length / 2) MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 1, tag); // Получаем следующее значение
                    first = num[0];
                }
                // Если закончились элементы во втором
                else if(second_k >= req_send.length / 2){
                    req_send[k] = first; // Присваиваем меньший
                    first_k++; // Обновляем счётчик для первого процессора
                    k++; // Обновляем общее количество добавленных элементов

                    // Выполняем проверку, если есть, что принимать - принимаем
                    if(first_k < req_send.length / 2) MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 1, tag); // Получаем следующее значение
                    first = num[0];
                }
                // Если закончились элементы в первом
                else if(first_k >= req_send.length / 2){
                    req_send[k] = second; // Присваиваем меньший
                    second_k++; // Обновляем счётчик для второго процессора
                    k++; // Обновляем общее количество добавленных элементов

                    // Выполняем проверку, если есть, что принимать - принимаем
                    if(second_k < req_send.length / 2) MPI.COMM_WORLD.Recv(num, 0, 1, MPI.DOUBLE, 2, tag); // Получаем следующее значение
                    second = num[0];
                }

            }
            System.out.println("Result = " + Arrays.toString(req_send));

        }

        MPI.Finalize();
    }
}
