import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Stack; 
import javafx.util.Pair; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class mine extends PApplet {




  int WindowHeight = 720;
  int WindowWidth = 1280;

class Box
{
  //coordinates
  int x,y;
  //Number of bombs in nearby area
  int vicinity;
  //Bomb
  int bomb;
  //Open or close
  int open;
  //Flag
  int flag;
  
  Box()
  {
  }
  
  Box(int b)
  {
    this.bomb=b;
  }
};

class Grid
{
  int x_offset,y_offset;
  int size;
  int windowHeight,windowWidth;
  int numRows,numCols;
  int numBombs,numFlags;
  int cursorR,cursorC;
  Box[][] F;

  boolean startGame;
  boolean gameOver;
  
  int startTime,endTime,elapsed;
  
  int vicinityColor = 0xff76d7c4;
  int highlightColor = 0xfff7dc6f; 
  int bombColor = 0xffff0000;
  int closedColor = 0xff808b96;
  int flagColor = 0xfff5b041; 
  int flagDisplayColor = 0xff17202a; 
  int topScreenColor = 0xffe5e7e9; 
  
  Grid(int s,int w,int h)
  {
    startGame =true;
    gameOver = false;
    size=s;
    x_offset = 0;
    y_offset = 40;
    windowHeight = h;
    windowWidth = w;
    numRows=windowHeight/size;
    numCols=windowWidth/size;
    F = new Box[numRows][numCols];
    numBombs = PApplet.parseInt(0.15f * numRows * numCols);
    numFlags = numBombs;
    int count = 0;
    for(int r=0;r<numRows;r++)
    {
      for(int c=0;c<numCols;c++)
      {        
        F[r][c] = new Box(0);
        F[r][c].open = 0;
      }
    }
    while(count<numBombs)
    {
      int tempr = PApplet.parseInt(random(0,numRows));
      int tempc = PApplet.parseInt(random(0,numCols));
      if(F[tempr][tempc].bomb != 1)
      {
        F[tempr][tempc].bomb = 1;
        print("R:" + tempr + " C:" + tempc + "\n");
        count++;
      }
    }
    for(int r=0;r<numRows;r++)
      for(int c=0;c<numCols;c++)
        if(F[r][c].bomb!=1) assignVicinity(r,c);
  }
  
  public void draw()
  { 
    topScreen();
    if(gameOver == true)
      gameOverScreen();
    cursorR = (mouseY-y_offset)/size;
    cursorC = (mouseX-x_offset)/size;
    rectMode(CORNER);
    //textMode(CENTER);
    for(int r=0;r<numRows;r++)
    {
      for(int c=0;c<numCols;c++)
      {
        if(r == cursorR && c == cursorC)
          fill(highlightColor);
        else if(F[r][c].open==0)
          fill(closedColor);
        else if(F[r][c].bomb == 1)
          fill(0);
        else
          fill(255);
        //if(r == cursorR && c == cursorC)
        //  stroke(highlightColor);
        //else
        //  stroke(0);
        rect(x_offset+c*size,y_offset+r*size,size,size);
        textAlign(CENTER,CENTER);
        textSize(PApplet.parseInt(0.75f*size));
        if(F[r][c].open == 1)
        {
          if(F[r][c].bomb != 1 && F[r][c].flag != 1)
          {
            fill(vicinityColor);
            text(F[r][c].vicinity,x_offset+c*size+size/2,y_offset+r*size+size/2);
          }
          else
          {
            fill(bombColor);
            text("X",x_offset+c*size+size/2,y_offset+r*size+size/2);
          }
        }
        if(F[r][c].flag == 1)
        {
          fill(flagColor);
          text("F",x_offset+c*size+size/2,y_offset+r*size+size/2);
        }
      }
    }
  }
  
  public void assignVicinity(int r,int c)
  {
    int count = 0;
    
    if(c != 0)
      count += F[r][c-1].bomb;
    if(c != numCols - 1)
      count += F[r][c+1].bomb;
    if(r != 0)
      count += F[r-1][c].bomb;
    if(r != numRows - 1)
      count += F[r+1][c].bomb;
    if((r != 0) && (c != 0))
      count += F[r-1][c-1].bomb;
    if((r != 0) && (c != numCols - 1))
      count += F[r-1][c+1].bomb;
    if((r != numRows - 1) && (c != 0))
      count += F[r+1][c-1].bomb;
    if((r != numRows - 1) && (c != numCols - 1))
      count += F[r+1][c+1].bomb;
    F[r][c].vicinity = count;
  } 
  
  public void placeFlag()
  {
    if(numFlags >= 0 && F[cursorR][cursorC].flag != 1)
    {
      F[cursorR][cursorC].flag=1;
      numFlags--;
    }
  }
  
  public void openSurrounding(int r,int c)
  {
    if(c != 0)
      F[r][c-1].open = 1;
    if(c != numCols - 1)
      F[r][c+1].open = 1;
    if(r != 0)
      F[r-1][c].open = 1;
    if(r != numRows - 1)
      F[r+1][c].open = 1;
    if((r != 0) && (c != 0))
      F[r-1][c-1].open = 1;
    if((r != 0) && (c != numCols - 1))
      F[r-1][c+1].open = 1;
    if((r != numRows - 1) && (c != 0))
      F[r+1][c-1].open = 1;
    if((r != numRows - 1) && (c != numCols - 1))
      F[r+1][c+1].open = 1;
  }
  
  public void clickOpen()
  {
    if(F[cursorR][cursorC].flag != 1)
      F[cursorR][cursorC].open = 1;
    if(F[cursorR][cursorC].bomb == 1)
      gameOver = true;
    Pair<Integer,Integer> cursorP = new Pair<Integer,Integer>(cursorR,cursorC);
    Stack<Pair<Integer,Integer>> S = new Stack<Pair<Integer,Integer>>();
    if(F[cursorR][cursorC].vicinity == 0 && F[cursorR][cursorC].flag!=1)
    {
      openSurrounding(cursorR,cursorC);
      Pair<Integer,Integer> nearby = zeroInVicinity(cursorP);
      print("Nearby(" + nearby.getKey() + "," + nearby.getValue() + ")\n");
      S.push(cursorP);
    }
    Pair<Integer,Integer> currentP;
    while(S.empty() == false)
    {
      currentP = S.peek();
      int r = currentP.getKey();
      int c = currentP.getValue();
      
      if(F[r][c].flag!=1)
        openSurrounding(r,c);
      
      Pair<Integer,Integer> nearby = zeroInVicinity(currentP);
      print("Current(" + currentP.getKey() + "," + currentP.getValue() + ")\n");
      
      if((nearby.getKey() != -1) && (nearby.getValue() != -1) && S.search(nearby) == -1)
      {
        print("Pushed(" + nearby.getKey() + "," + nearby.getValue() + ")\n");
        S.push(nearby);
      }
      else
      {
        Pair temp = S.pop();
        print("Popped(" + temp.getKey() + "," + temp.getValue() + ")\n");
      }
    }
  }
  
  public Pair<Integer,Integer> zeroInVicinity(Pair<Integer,Integer> P)
  {
    int r = P.getKey();
    int c = P.getValue();
    
    if(c != 0) 
      if(F[r][c-1].vicinity == 0 && isSurroundingOpen(r,c-1) == false)
      {  
      Pair<Integer,Integer> temp = new Pair<Integer,Integer>(r,c-1);
      return temp;
      }
    if(c != numCols - 1)
      if(F[r][c+1].vicinity == 0 && isSurroundingOpen(r,c+1) == false)
      {
      Pair<Integer,Integer> temp = new Pair<Integer,Integer>(r,c+1);
      return temp;
      }
    if(r != 0)
      if(F[r-1][c].vicinity == 0 && isSurroundingOpen(r-1,c) == false)
      {
      Pair<Integer,Integer> temp = new Pair<Integer,Integer>(r-1,c);
      return temp;
      }
    if(r != numRows - 1)
      if(F[r+1][c].vicinity == 0 && isSurroundingOpen(r+1,c) == false)
      {
      Pair<Integer,Integer> temp = new Pair<Integer,Integer>(r+1,c);
      return temp;
      }
    if((r != 0) && (c != 0))
      if(F[r-1][c-1].vicinity == 0 && isSurroundingOpen(r-1,c-1) == false)
      {
      Pair<Integer,Integer> temp = new Pair<Integer,Integer>(r-1,c-1);
      return temp;
      }
    if((r != 0) && (c != numCols - 1))
      if(F[r-1][c+1].vicinity == 0 && isSurroundingOpen(r-1,c+1) == false)
      {
      Pair<Integer,Integer> temp = new Pair<Integer,Integer>(r-1,c+1);
      return temp;
      }
    if((r != numRows - 1) && (c != 0))
      if(F[r+1][c-1].vicinity == 0 && isSurroundingOpen(r+1,c-1) == false)
      {
      Pair<Integer,Integer> temp = new Pair<Integer,Integer>(r+1,c-1);
      return temp;
      }
    if((r != numRows - 1) && (c != numCols - 1))
      if(F[r+1][c+1].vicinity == 0 && isSurroundingOpen(r+1,c+1) == false)
      {
      Pair<Integer,Integer> temp = new Pair<Integer,Integer>(r+1,c+1);
      return temp;
      }
    Pair<Integer,Integer> temp = new Pair<Integer,Integer>(-1,-1);
    return temp;
  }
  
  public boolean isSurroundingOpen(int r,int c)
  {
    if(c != 0)
      if(F[r][c-1].open == 0) return false;
    if(c != numCols - 1)
      if(F[r][c+1].open == 0) return false;
    if(r != 0)
      if(F[r-1][c].open == 0) return false;
    if(r != numRows - 1)
      if(F[r+1][c].open == 0) return false;
    if((r != 0) && (c != 0))
      if(F[r-1][c-1].open == 0) return false;
    if((r != 0) && (c != numCols - 1))
      if(F[r-1][c+1].open == 0) return false;
    if((r != numRows - 1) && (c != 0))
      if(F[r+1][c-1].open == 0) return false;
    if((r != numRows - 1) && (c != numCols - 1))
      if(F[r+1][c+1].open == 0) return false;
    return true;
  }
  
  public void gameOverScreen()
  {
      for(int r=0;r<numRows;r++)
        for(int c=0;c<numCols;c++)
          F[r][c].open=1;
      textAlign(CENTER,CENTER);
      fill(bombColor);
      text("Game Over, Press 'R' to reset",PApplet.parseInt(windowWidth/2+x_offset),PApplet.parseInt(y_offset/2));
  }

  public void flagScreen()
  {
    textAlign(LEFT,CENTER);
    textSize(0.6f*y_offset);
    String flagDisplay = "Flags:" + numFlags;
    fill(flagDisplayColor);
    text(flagDisplay,0,y_offset/2);
  }
  
  public void topScreen()
  {
    rectMode(CORNER);
    fill(topScreenColor);
    rect(0,0,windowWidth,y_offset);
    flagScreen();
    timeScreen();
  }
  
  public void timeScreen()
  {
    endTime = (hour() * 3600) + (minute() * 60) + (second());
    if(gameOver != true)
      elapsed = endTime - startTime;
    int elapsedH = PApplet.parseInt(elapsed/3600);
    elapsed %= 3600;
    int elapsedM = PApplet.parseInt(elapsed/60);
    elapsed %= 60;
    int elapsedS = elapsed;
    textAlign(RIGHT,CENTER);
    textSize(0.6f*y_offset);
    fill(flagDisplayColor);
    if(startGame != true)
      text(elapsedH + ":" + elapsedM + ":" + elapsedS,windowWidth,y_offset/2);
    else
      text(0 + ":" + 0 + ":" + 0,windowWidth,y_offset/2);
  }
  
  public void startTime()
  {
    startTime = (hour() * 3600) + (minute() * 60) + (second());
    startGame = false;
  }
};

Grid obj;

public void setup()
{
  
  obj = new Grid(40,1280,680);
}

public void draw()
{
  obj.draw();
  //print("R:" + obj.numRows + " C:" + obj.numCols + "\n");
}

public void mouseClicked()
{
  if(obj.startGame == true)
    obj.startTime();
  if(mouseButton == LEFT)
    obj.clickOpen();
  else if(mouseButton == RIGHT)
    obj.placeFlag();
}

public void keyPressed()
{
  if(key == 'r')
    setup();
}
  public void settings() {  size(1280,720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "mine" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
