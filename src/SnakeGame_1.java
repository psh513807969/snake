

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnakeGame_1 extends JPanel{
    private static final long serialVersionUID = 1L;
	int score;
    //常量，表示地图的宽和高
     protected static final int HEIGHT=30;
      protected static final int WIDTH=30;
        //常量，表示每个方格的宽和高
       protected static final int CELL_H=20;
       protected static final int CELL_W=20;
   //常量，表示蛇的方向
       protected static final int UP_DIRECTION=1;
       protected static final int DOWN_DIRECTION=-1;
       protected static final int LEFT_DIRECTION=2;
       protected static final int RIGHT_DIRECTION=-2;
        //当前方向，默认为右方
        private int currentdirection=RIGHT_DIRECTION;
        //char型数组来储存地图
        protected char[][]  map=new char[HEIGHT][WIDTH];
        //容器，用来储存蛇的坐标信息
        private LinkedList<Point>  snake=new LinkedList<Point>();
        //食物的坐标用Point类来储存
        private Point food=new Point();

        //用来判断游戏是否结束
        protected static boolean GameOver=false;
        //判断游戏是否暂停
        private static boolean flagPause = false;	
        protected static boolean IsAuto=true;
        private static int Difficult_Degree=1;
    
       
        

/* 图形刷新区 */
        //重写Paint方法
        //*代表石头
        @Override
        public void paint(Graphics g) {
       
        	g.setFont(new Font("黑体", Font.BOLD,25 ));
        	g.drawString("贪吃蛇玩法说明",610, 100);
        	g.setFont(new Font("宋体", Font.BOLD,15 ));
        	g.drawString("红色蛇头 蓝色食物",630,160);
        	g.drawString("一个食物一分",650,200);
        	g.drawString("上下是石头 左右可以通过",608,240);
        	g.drawString("方向键控制方向",645,280);
        	g.drawString("小键盘+，-控制速度",625,320);
        	g.drawString("空格暂停",670,360);
        	g.drawString("ESC关闭",670,400);
        	g.clearRect(745, 425, 30, 20);
        	g.clearRect(745, 465, 30, 20);
        	g.drawString("您的分数为："+score,650,440);
        	g.drawString("当前速度为："+Difficult_Degree,650,480);
        	
        	
        //画地图
        for(int i=0;i<HEIGHT;i++)
            for(int j=0;j<WIDTH;j++)
            {
                if(map[i][j]=='*'){
                    g.setColor(Color.GRAY);
                }else{
                    g.setColor(Color.WHITE);
                }
                g.fill3DRect(j*CELL_W, i*CELL_H, CELL_W, CELL_H, true);
            }


        //画蛇
        //定位蛇头
        int W=snake.getFirst().x;
        int H=snake.getFirst().y;
        g.setColor(Color.RED);
        g.fill3DRect(W*CELL_W,H*CELL_H , 20, 20,true);
        //定位蛇身
        for(int t=1;t<this.snake.size();t++)
        {
            W=snake.get(t).x;
            H=snake.get(t).y;
            g.setColor(Color.GREEN);
            g.fill3DRect(W*CELL_W,H*CELL_H , 20, 20,true);

        }

        //画食物@
        map[food.y][food.x]='@';
        g.setColor(Color.BLUE);
        g.fill3DRect(food.x*CELL_W, food.y*CELL_H, 20, 20, true);


        //画字
        if(SnakeGame_1.GameOver){
            g.setColor(Color.ORANGE);
            g.setFont(new Font("宋体", Font.BOLD,30 ));
            g.drawString("游戏结束 !", CELL_W*(WIDTH/3), CELL_H*(HEIGHT/2));
        }
        if(SnakeGame_1.flagPause){
            g.setColor(Color.ORANGE);
            g.setFont(new Font("宋体", Font.BOLD,30 ));
            g.drawString("游戏暂停 !", CELL_W*(WIDTH/3), CELL_H*(HEIGHT/2));
        }




}
/*  初始化地图、蛇、食物函数区  */   
        //初始化Map
        public void intiMap() {

            for(int i=0;i<HEIGHT;i++)
                for(int j=0;j<WIDTH;j++)
                {	
              
                    if(i==0||(i==HEIGHT-1)){
                        map[i][j]='*';
                    }
                    else this.map[i][j]=' ';
                }
        }
        //初始化snake
        public void intiSnake(){
            int x=WIDTH/2;
            int y=HEIGHT/2;
            snake.addFirst(new Point(x-1,y));
            snake.addFirst(new Point(x, y));
            snake.addFirst(new Point(x+1,y));
        }
        //初始化食物
        public void intiFood(){
            while(true){
                Random random=new Random();
                int x=random.nextInt(WIDTH-1);
                int y=random.nextInt(HEIGHT-1);
                if(map[y][x]!='*'&&map[y][x]!='#'&&map[y][x]!='$')
                {
                    food.x=x;
                    food.y=y;
                    break;

                }
            } 
        }
     
      
        
        
/*  蛇移动函数区  */
        //run函数，蛇自己走
        public void runAuto() throws InterruptedException{
            while (true){
            	if(GameOver)
            		break;
            	if (flagPause)
            	{
            		Thread.sleep(10);
            		continue;
            	}
            	
                move();
                IsOver();
                reFresh();
                repaint();

                if(GameOver){
                	repaint();
                }
                Thread.sleep(1000/Difficult_Degree);
               if(flagPause){
                   repaint();
                		
                   }
               
                }  
        }
        
        
        

        //蛇移动函数
        public void move(){
            //取蛇头
            Point snakehead=snake.getFirst();
            //依据方向，移动
            switch(currentdirection){
            //向上移动
            case UP_DIRECTION :
                //结束的时候不执行添加头结点
                if(!GameOver){
                	if(!flagPause){
                    snake.addFirst(new Point(snakehead.x,snakehead.y-1));
                	}
                }

                break;
            //向下移动
            case DOWN_DIRECTION :
                //结束的时候不执行添加头结点
                if(!GameOver){
                	if(!flagPause){
                     snake.addFirst(new Point(snakehead.x,snakehead.y+1));
                	}
                }

                break;
            //向左移动
            case LEFT_DIRECTION :
                if(!GameOver){
                	if(!flagPause){
                    if(snakehead.x==0){
                    snake.addFirst(new Point(snakehead.x-1+WIDTH,snakehead.y));
                }else{
                    snake.addFirst(new Point(snakehead.x-1,snakehead.y));
                	}
                	}
                }

            break;
            //向右移动
            case RIGHT_DIRECTION :
                if(!GameOver){
                	if(!flagPause){
                    snake.addFirst(new Point((snakehead.x+1)%WIDTH,snakehead.y));
                
                	}else{
                        snake.addFirst(new Point(snakehead.x+1,snakehead.y));
                    	}
                }
           
            break;
            default : break;
            }
           
            if(eatFood()){
                //先刷新，防止出现食物长到身上
                repaint();
                //重建食物
                intiFood();
            }else{
                //游戏结束的时候不执行删除尾节点
                if(!GameOver){
                    //删除蛇尾
                    snake.removeLast();
                }

            }

        }
        //改变蛇的方向
        public void changeDirection(int newDirection){
            if(newDirection+currentdirection!=0){
                currentdirection=newDirection;
            }
        }
        //蛇吃食物区 
        public boolean eatFood(){
            //取蛇头
            Point snakehead=snake.getFirst();
            if(snakehead.equals(food)){
            	score+=1;
                return true;
                
            }
            return false;
        }

/*  刷新区  */
        public void  reFresh(){
            //刷新地图
            intiMap();
            //刷新蛇
            showSnake();
            //刷新食物
            showFood();
        
     }
/*  打印函数区*/
        //打印输出地图
         public boolean showMap(){
            {
                for(int i=0;i<HEIGHT;i++){
                for(int j=0;j<WIDTH;j++)
                {
                    System.out.print(this.map[i][j]);
                }
                System.out.println();
            }
                return true;
            }
            }
        //在地图上显示蛇
        public boolean showSnake(){
            {
                    //定位蛇头
                    int W=snake.getFirst().x;
                    int H=snake.getFirst().y;
                    map[H][W]='$';
                    //定位蛇身
                    for(int i=1;i<this.snake.size();i++)
                    {
                        W=snake.get(i).x;
                        H=snake.get(i).y;
                        map[H][W]='#';
                    }
                return true;    
                }
            }
        //在地图上显示食物
        public boolean showFood(){
            {
                map[food.y][food.x]='@';
                return true;
            }
        }
     
/* 设置游戏速度区  */
        public void set_Difficuty(){
      
            }

/*  结束游戏区  */
        public void IsOver(){
            //取蛇头
            Point snakehead=snake.getFirst();
            //撞墙死
            if(map[snakehead.y][snakehead.x]=='*'){
                GameOver=true;
            }
            //咬到自己死
            if(map[snakehead.y][snakehead.x]=='#'){
                GameOver=true;
            }
        }


    public static void main(String[] args) throws InterruptedException {

//图形界面
       final SnakeGame_1 sg1=new SnakeGame_1();
       sg1.set_Difficuty();
       JFrame frame1=new JFrame("贪吃蛇");//设置窗口名字
       frame1.setSize(CELL_W*WIDTH+220, CELL_H*HEIGHT+45);//窗口大小及其位置
       frame1.setVisible(true);
       frame1.add(sg1);
       sg1.intiMap();
       sg1.intiSnake();
       sg1.intiFood();
       sg1.repaint();


      
       //为窗口添加监听器
       frame1.addKeyListener(new KeyAdapter() {

    	   
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch(e.getKeyCode()){
                case KeyEvent.VK_UP:
                    IsAuto=false;
                    sg1.changeDirection(UP_DIRECTION);
                    break;
                case KeyEvent.VK_DOWN:
                    IsAuto=false;
                    sg1.changeDirection(DOWN_DIRECTION);
                    break;
                case KeyEvent.VK_LEFT:
                    IsAuto=false;
                    sg1.changeDirection(LEFT_DIRECTION);
                    break;
                case KeyEvent.VK_RIGHT:
                    IsAuto=false;
                    sg1.changeDirection(RIGHT_DIRECTION);
                    break;
                case KeyEvent.VK_SPACE:
                    if(flagPause){
                     	flagPause=false;
                     	
                     }else{
                     	flagPause=true;
                     }
                   
                    break;
                case KeyEvent.VK_ESCAPE:
                	System.exit(0);
                	break;
                case KeyEvent.VK_ADD:
                	if(Difficult_Degree<=9){
                	Difficult_Degree++;
                	}
                	break;
                case KeyEvent.VK_SUBTRACT:
                	if(Difficult_Degree>1){
                	Difficult_Degree--;
                	}
                	break;
              
                default: break;
                }
                if (!flagPause)
                {
                sg1.move();
                sg1.IsOver();
                sg1.reFresh();
                sg1.repaint();
                }
              
                if(SnakeGame_1.GameOver){
                    sg1.repaint();
                
              }
             //   IsAuto=true;
            }

       });
       //如果IsAuto 为真，则自己走
      if(IsAuto)sg1.runAuto();  
    }
}

