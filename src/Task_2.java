import mpi.*;

public class Task_2 {
    public static void main(String[] args) {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int tag = 100;


        if(myrank % 2 == 0){
            if(myrank + 1 != size){
                int[] numbertosend = new int[1];
                numbertosend[0] = 100+myrank;
                System.out.println("This is process = "+myrank+"" + "    Sending : "  + numbertosend[0]);
                MPI.COMM_WORLD.Send(numbertosend,0,  1, MPI.INT, myrank+1, tag);
            }
        }
        else{
            int[] numbertorecv = new int[1];
            MPI.COMM_WORLD.Recv(numbertorecv, 0, 1, MPI.INT, MPI.ANY_SOURCE, tag);
            System.out.println("This is process = "+ myrank+ " " + "   Receive from = "+ (myrank-1) + "    Receive: " + numbertorecv[0]);
        }

        MPI.Finalize();
    }

}