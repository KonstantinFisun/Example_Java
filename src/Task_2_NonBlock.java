import mpi.*;

public class Task_2_NonBlock {
    public static void main(String[] args) {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        Request[] req = new Request[size-1];
        Status[] sta = new Status[size-1];
        int[] number = new int[1];

        int tag = 100;
        int s;

        if(myrank == 0){
            number[0] = myrank;
            req[myrank]=MPI.COMM_WORLD.Isend(number, 0, 1, MPI.INT, myrank+1, tag);
            sta[myrank]=req[myrank].Wait();
            if (req[myrank].Is_null())  System.out.println(myrank + " send to "+ (myrank + 1) + " complete");
        } else if (myrank + 1 == size) {
            MPI.COMM_WORLD.Recv(number, 0, 1, MPI.INT, myrank-1, tag);
            System.out.println("This is last process = "+ myrank+ " " + "   Receive from = "+ (myrank-1) + " Receive: " + number[0]);
            number[0] += myrank;
            System.out.println("Total sum = " + number[0]);
        }
        else{
            MPI.COMM_WORLD.Recv(number, 0, 1, MPI.INT, myrank-1, tag);
            System.out.println("This is process = "+ myrank+ " " + "   Receive from = "+ (myrank-1) + " Receive: " + number[0] + " Sending : " + myrank);
            number[0] += myrank;
            req[myrank]=MPI.COMM_WORLD.Isend(number, 0, 1, MPI.INT, myrank+1, tag);
            sta[myrank]=req[myrank].Wait();
            if (req[myrank].Is_null())  System.out.println(myrank + " send to "+ (myrank + 1) + " complete");
        }



        MPI.Finalize();
    }

}
