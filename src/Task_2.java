import mpi.*;

public class Task_2 {
    public static void main(String[] args) {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int tag = 100;
        int s;
        int buf;

        if(myrank == 0){
            buf = myrank;
            System.out.println("This is process = "+ myrank+ " " + "Sending : " + buf);
            int[] numbertosend = new int[1];
            numbertosend[0] = buf;
            MPI.COMM_WORLD.Send(numbertosend,0,  1, MPI.INT, myrank+1, tag);
        } else if (myrank + 1 == size) {
            buf = myrank;
            int[] numbertorecv = new int[1];
            MPI.COMM_WORLD.Recv(numbertorecv, 0, 1, MPI.INT, myrank-1, tag);
            System.out.println("This is process = "+ myrank+ " " + "   Receive from = "+ (myrank-1) + " Receive: " + numbertorecv[0] + " Sending : " + buf);

            s = numbertorecv[0] + buf;

            System.out.println("Sum : " + s);
        }
        else{
            buf = myrank;
            int[] numbertorecv = new int[1];
            MPI.COMM_WORLD.Recv(numbertorecv, 0, 1, MPI.INT, myrank-1, tag);
            System.out.println("This is process = "+ myrank+ " " + "   Receive from = "+ (myrank-1) + " Receive: " + numbertorecv[0] + " Sending : " + buf);

            s = numbertorecv[0] + buf;

            int[] numbertosend = new int[1];
            numbertosend[0] = buf;
            MPI.COMM_WORLD.Send(numbertosend,0,  1, MPI.INT, myrank+1, tag);
        }



        MPI.Finalize();
    }

}