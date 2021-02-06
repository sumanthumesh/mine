import java.util.Stack;
import javafx.util.Pair;

class Solver
{
  float[][] prob;
  int numRows,numCols;
  Grid G;
  int hint_status,hint_r,hint_c;
  
  color red =  #ff0000;
  color green = #00ff00;
  color yellow = #ffff00;
  
  Solver(Grid g)
  {
    G = g;
    numRows = G.numRows;
    numCols = G.numCols;
    prob = new float[numRows][numCols];
    hint_status = 0;
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
    if(G.F[r][c].flag == 1)
      return 2;
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
  
  Pair<Integer,Integer> findLeastPofE()
  {
    float least_prob = 2;
    Pair<Integer,Integer> P = new Pair<Integer,Integer>(0,0);
    
    for(int r = 0; r < numRows; r++)
    {
      for(int c = 0;c < numCols; c++)
      {
        if(G.F[r][c].flag != 1 && G.F[r][c].open == 0)
        {
          if(findPofE(r,c) == 0)
          {
            P = new Pair<Integer,Integer>(r,c);
            return P;
          }
          if(findPofE(r,c)<least_prob)
          {
            P = new Pair<Integer,Integer>(r,c);
            least_prob = findPofE(r,c);
          }
        }
      }
    }
    return P;
  }
  
  Pair<Integer,Integer> findBomb()
  {
    Pair<Integer,Integer> P =new Pair<Integer,Integer>(-1,-1);
    for(int r=0;r<numRows;r++)
    {
      for(int c=0;c<numCols;c++)
      {
        if(G.F[r][c].flag==0 && G.F[r][c].open == 0 && findPofE(r,c) == 1)
        {
          P = new Pair<Integer,Integer>(r,c);
          return P;
        }
      }  
    }
    return P;
  }
  
  void highlightRed(int r,int c)
  {
    fill(red);
    circle(G.x_offset+c*G.size+G.size/2,G.y_offset+r*G.size+G.size/2,G.size/2);
  }
  
  void highlightGreen(int r,int c)
  {
    fill(green);
    circle(G.x_offset+c*G.size+G.size/2,G.y_offset+r*G.size+G.size/2,G.size/2);
  }
  
  void highlightYellow(int r,int c)
  {
    fill(yellow);
    circle(G.x_offset+c*G.size+G.size/2,G.y_offset+r*G.size+G.size/2,G.size/2);
  }
   
  
  void getHint()
  {
    Pair<Integer,Integer> P = findBomb();
    Pair<Integer,Integer> Q = findLeastPofE();
    if(P.getKey() != -1 || P.getValue() != -1)
    {
      hint_status =1;
      hint_r = P.getKey();
      hint_c = P.getValue();
      print("Red: " + hint_r + hint_c + "\n");
    }
    else if(findPofE(Q.getKey(),Q.getValue())==0)
    {
      hint_status =2;
      hint_r = Q.getKey();
      hint_c = Q.getValue();
      print("Green: " + hint_r + hint_c + "\n");
    }
    else 
    {
      hint_status =3;
      hint_r = Q.getKey();
      hint_c = Q.getValue();
      print("Yellow: " + hint_r + hint_c + "\n");
    }
  }
  
  void draw()
  {
    if(keyPressed == true){
    if(hint_status == 1)
      highlightRed(hint_r,hint_c);
    else if(hint_status == 2)
      highlightGreen(hint_r,hint_c); 
    else if(hint_status == 3)
      highlightYellow(hint_r,hint_c);
    }
    //hint_status = 0;
  }
};
