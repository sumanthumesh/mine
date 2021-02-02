import java.util.Stack;
import javafx.util.Pair;

class Solver
{
  float[][] prob;
  int numRows,numCols;
  Grid G;
  
  Solver(Grid g)
  {
    G = g;
    numRows = G.numRows;
    numCols = G.numCols;
    prob = new float[numRows][numCols];
  }
  
  float findContribution(int r,int c)
  {
    if(G.F[r][c].open == 0 || G.F[r][c].flag == 1)
      return -1;
    //G = g;
    int num_candidates = 0,num_flags = 0;
    if(c != 0)
    {
      if(G.F[r][c-1].open == 0)
      num_candidates++;
      if(G.F[r][c-1].flag == 1)
      num_flags++;
    }
    if(c != numCols - 1)
    {
      if(G.F[r][c+1].open == 0)
      num_candidates++;
      if(G.F[r][c+1].flag == 1)
      num_flags++;
    }
    if(r != 0)
    {
      if(G.F[r-1][c].open == 0)
      num_candidates++;
      if(G.F[r-1][c].flag == 1)
      num_flags++;
    }
    if(r != numRows - 1)
    {
      if(G.F[r+1][c].open == 0)
      num_candidates++;
      if(G.F[r+1][c].flag == 1)
      num_flags++;
    }
    if((r != 0) && (c != 0))
    {
      if(G.F[r-1][c-1].open == 0)
      num_candidates++;
      if(G.F[r-1][c-1].flag == 1)
      num_flags++;
    }
    if((r != 0) && (c != numCols - 1))
    {
      if(G.F[r-1][c+1].open == 0)
      num_candidates++;
      if(G.F[r-1][c+1].flag == 1)
      num_flags++;
    }
    if((r != numRows - 1) && (c != 0))
    {
      if(G.F[r+1][c-1].open == 0)
      num_candidates++;
      if(G.F[r+1][c-1].flag == 1)
      num_flags++;
    }
    if((r != numRows - 1) && (c != numCols - 1))
    {
      if(G.F[r+1][c+1].open == 0)
      num_candidates++;
      if(G.F[r+1][c+1].flag == 1)
      num_flags++;
    }
    //print("Candidates:"+num_candidates+"\n");
    //print("Flags:"+num_flags+"\n");
    //print("Vicinity:"+G.F[r][c].vicinity+"\n");
    if((num_candidates-num_flags)==0)
      return -1;
    return float((G.F[r][c].vicinity-num_flags))/float((num_candidates-num_flags));
  }
  
  float findPofE(int r,int c)
  {
    float highest_prob = -1;
    if(c != 0)
    {
      if(findContribution(r,c-1) > highest_prob)
      highest_prob = findContribution(r,c-1);
      if(findContribution(r,c-1) == 0)
      return 0;  
    }
    if(c != numCols - 1)
    {
      if(findContribution(r,c+1) > highest_prob)
      highest_prob = findContribution(r,c+1);
      if(findContribution(r,c+1) == 0)
      return 0;  ;
    }
    if(r != 0)
    {
      if(findContribution(r-1,c) > highest_prob)
      highest_prob = findContribution(r-1,c);
      if(findContribution(r-1,c) == 0)
      return 0;  
    }
    if(r != numRows - 1)
    {
      if(findContribution(r+1,c) > highest_prob)
      highest_prob = findContribution(r+1,c);
      if(findContribution(r+1,c) == 0)
      return 0;  
    }
    if((r != 0) && (c != 0))
    {
      if(findContribution(r-1,c-1) > highest_prob)
      highest_prob = findContribution(r-1,c-1);
      if(findContribution(r-1,c-1) == 0)
      return 0;   
    }
    if((r != 0) && (c != numCols - 1))
    {
      if(findContribution(r-1,c+1) > highest_prob)
      highest_prob = findContribution(r-1,c+1);
      if(findContribution(r-1,c+1) == 0)
      return 0;  
    }
    if((r != numRows - 1) && (c != 0))
    {
      if(findContribution(r+1,c-1) > highest_prob)
      highest_prob = findContribution(r+1,c-1);
      if(findContribution(r+1,c-1) == 0)
      return 0;  
    }
    if((r != numRows - 1) && (c != numCols - 1))
    {
      if(findContribution(r+1,c+1) > highest_prob)
      highest_prob = findContribution(r+1,c+1);
      if(findContribution(r+1,c+1) == 0)
      return 0;  
    }
    if(highest_prob == -1)
      return 2;
    return highest_prob;
  }
};
