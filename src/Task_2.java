import mpi.*;

public class Task_2 {
    public static void main(String[] args) {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int tag = 100;
        int[] s = new int[1];

        if(myrank == 0){
            System.out.println("This is process = "+ myrank+ " " + "Sending : " + myrank);
            s[0] = myrank;
            MPI.COMM_WORLD.Send(s,0,  1, MPI.INT, myrank+1, tag);
        } else if (myrank + 1 == size) {
            MPI.COMM_WORLD.Recv(s, 0, 1, MPI.INT, MPI.ANY_SOURCE, tag);
            System.out.println("This is process = "+ myrank+ " " + "   Receive from = "+ (myrank-1) + " Receive: " + s[0] + " Sending : " + myrank);
            s[0] += myrank;
        }
        else{
            MPI.COMM_WORLD.Recv(s, 0, 1, MPI.INT, MPI.ANY_SOURCE, tag);
            System.out.println("This is process = "+ myrank+ " " + "   Receive from = "+ (myrank-1) + " Receive: " + s[0] + " Sending : " + myrank);

            s[0] += myrank;
            MPI.COMM_WORLD.Send(s,0,  1, MPI.INT, myrank+1, tag);
        }



        MPI.Finalize();
        System.out.println("Sum : " + s[0]);
    }

}