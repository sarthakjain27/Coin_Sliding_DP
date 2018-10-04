import java.io.*;
import java.util.*;
import java.math.*;

public class Solution {
	static int[] minimum_changes(char[][] board) {
        int num_row=board.length;
        int num_col=board[0].length;
        int prev[]=new int[num_col];
        prev[0]=0;
        for(int i=1;i<num_col;i++)
        {
            if(board[0][i-1]=='R')
                prev[i]=prev[i-1];
            else
                prev[i]=prev[i-1]+1;
        }
    
        for(int rowiter=1;rowiter<num_row;rowiter++)
        {
            int ahead[]=new int[num_col];
            for(int coliter=0;coliter<num_col;coliter++)
            {
                if(coliter!=0)
                {
                    int above=(board[rowiter-1][coliter]=='D')?prev[coliter]:prev[coliter]+1;
                    int left=(board[rowiter][coliter-1]=='R')?ahead[coliter-1]:ahead[coliter-1]+1;
                    ahead[coliter]=Math.min(above,left);
                }
                else
                {
                    if(board[rowiter-1][coliter]=='D')
                        ahead[coliter]=prev[coliter];
                    else
                        ahead[coliter]=prev[coliter]+1;
                }
            }
            for(int coliter=num_col-2;coliter>=0;coliter--)
            {
                int right=(board[rowiter][coliter+1]=='L')?ahead[coliter+1]:ahead[coliter+1]+1;
                ahead[coliter]=Math.min(ahead[coliter],right);
            }
            prev=ahead;
        }
        
        for(int i=0;i<num_col;i++)
        {
            if(board[num_row-1][i]!='D')
                prev[i]=prev[i]+1;
        }
        return prev;
    }
	
	// see http://www.jstatsoft.org/v08/i14/paper for definition of xorshift RNG
    static int seed = 123456;
    static int xorshift() {
        seed ^= (seed << 1);
        seed ^= (seed >> 3);
        seed ^= (seed << 10);
        return seed;
    }

    static final String directions = new String("LDRLDRLDRLDRLDR*");

    public static void main(String[] args) throws IOException{
        Scanner in = new Scanner(System.in);
        final String fileName = System.getenv("OUTPUT_PATH");
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

        int _board_rows = Integer.parseInt(in.nextLine().trim());
        int _board_cols = Integer.parseInt(in.nextLine().trim());
        char[][] _board = new char[_board_rows][_board_cols];
        if (_board_cols < 100 && _board_rows < 100) {
            for (int _board_i=0; _board_i<_board_rows; _board_i++) {
                String line = in.nextLine().trim();
                for (int _board_j=0; _board_j<_board_cols; _board_j++) {
                    _board[_board_i][_board_j] = line.charAt(_board_j);
                }
            }
        } else {
            seed = Integer.parseInt(in.nextLine().trim());
            int dirlen = directions.length();
            for (int _board_i=0; _board_i<_board_rows; _board_i++) {
                for (int _board_j=0; _board_j<_board_rows; _board_j++) {
                    int idx = ((xorshift()&0x7FFFFFFF) + dirlen) % dirlen;
                    _board[_board_i][_board_j] = directions.charAt(idx);
                }
            }
        }

        int[] res = minimum_changes(_board);
        for(int res_i=0; res_i < res.length; res_i++) {
            bw.write(String.valueOf(res[res_i]));
            bw.newLine();
        }
        bw.write("DONE");
        bw.newLine();
        bw.close();
    }
}