package com.sarahdev.chinesecheckers.play;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceView;


import androidx.core.content.ContextCompat;

import com.sarahdev.chinesecheckers.R;
import com.sarahdev.chinesecheckers.model.MyPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayView extends SurfaceView {
    protected Context context;
    protected Map<Integer, List<MyPoint>> nodesMap;
    protected Map<Integer, List<MyPoint>> playersMarbles;
    protected List<Integer> colors;
    private String name ;
    private boolean isHuman;
    private int indexCurrent = 0;
    private int rounds;

    private final int delay;
    private int pause = 0;
    private List<MyPoint> visited, queue;
    private List<MyPoint> possibles = null;

    private int width, height, centerX, centerY, nodeRadius, nodeGap;
    private final Paint paintBrush = new Paint();
    private final int[] teamColor = new int [] {R.color.team0, R.color.team1, R.color.team2, R.color.team3, R.color.team4, R.color.team5, R.color.node};
    private final int[] teamColorBright = new int [] {R.color.team0_bright, R.color.team1_bright, R.color.team2_bright, R.color.team3_bright, R.color.team4_bright, R.color.team5_bright};

    public PlayView(Context context) {
        super(context);
        this.context = context;
        this.delay = 40;
    }
    public PlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.delay = 40;
    }
    public PlayView(Context context, Map<Integer, List<MyPoint>> nodesMap, Map<Integer, List<MyPoint>> playersMarbles, List<Integer> colors, int delay) {
        super(context);
        this.context = context;
        this.nodesMap = nodesMap;
        this.playersMarbles = playersMarbles;
        this.colors = colors;
        this.delay = delay;
    }

    public void setParams(Map<Integer, List<MyPoint>> playersPegs) {
        this.playersMarbles = playersPegs;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.setBackgroundResource(R.drawable.blur_background);

        width = getWidth();
        height = getHeight();
        centerX = (int) (width*0.5);
        centerY = (int) (height*0.5);
        nodeRadius = (int) (width*0.03);
        nodeGap = (int) (width * 0.075);

        paintBrush.setAntiAlias(true);
        paintBrush.setStrokeWidth(4);
        drawNodes(canvas);
        drawMarbles(canvas);
        drawHeader(canvas);

        if (possibles != null)
            drawPossibles(canvas);
        if (!isHuman)
            drawComputerMove(canvas);
        else
            drawHumanMove(canvas);
    }

    public void displayGame(int indexCurrent, boolean isHuman, Map<Integer, List<MyPoint>> playersPegs, List<MyPoint> way, int rounds, String name) {
        this.indexCurrent = indexCurrent;
        this.isHuman = isHuman;
        this.playersMarbles = playersPegs;
        setWay(way);
        this.rounds = rounds;
        this.possibles = null;
        this.name = name;
    }

    public void drawNodes(Canvas canvas) {
        for (Map.Entry<Integer, List<MyPoint>> entry : nodesMap.entrySet())
            for (MyPoint node : entry.getValue()) {
                fillNode(canvas, node, ContextCompat.getColor(context, R.color.board));
                surroundNode(canvas, node, ContextCompat.getColor(context, teamColor[entry.getKey()]));
            }
    }

    public void drawMarbles(Canvas canvas) {
         for (Map.Entry<Integer, List<MyPoint>> entry : playersMarbles.entrySet())
             for (MyPoint marble : entry.getValue())
                 fillNode(canvas, marble, ContextCompat.getColor(context, teamColor[entry.getKey()]));
     }

    public void drawHeader(Canvas canvas) {
        paintBrush.setColor(ContextCompat.getColor(context, R.color.primaryColorDark));
        canvas.drawRect(0,0,width, 70, paintBrush);
        paintBrush.setColor(Color.WHITE);
        paintBrush.setTextSize(40);
        String round = "Tour de jeu : " + rounds;
        canvas.drawText(round, (int) (width*0.1), 50, paintBrush);
        canvas.drawText("-", width, 50, paintBrush);
        canvas.drawText(name, (int) (width*0.60), 50, paintBrush);
    }

    private void drawHumanMove(Canvas canvas) {
        if (visited != null && visited.size() > 0) {
            fillNode(canvas, visited.get(0), ContextCompat.getColor(context, teamColorBright[colors.get(indexCurrent)]));
            if (queue != null && queue.size() > 1)
                for (int i = 0; i < queue.size() - 1; i++)
                    fillNode(canvas, queue.get(i), Color.rgb(166, 155, 151));
            MyPoint end;
            if (queue != null && queue.size() > 0)
                end = queue.get(queue.size() - 1);
            else
                end = visited.get(0);
            fillNode(canvas, end, ContextCompat.getColor(context, teamColor[colors.get(indexCurrent)]));
            surroundNode(canvas, end, ContextCompat.getColor(context, teamColor[colors.get((indexCurrent+3)%6)]));
        }
    }

    private void drawComputerMove(Canvas canvas) {
        if (queue.size() > 0)
            fillNode(canvas, queue.get(queue.size() - 1), ContextCompat.getColor(context, R.color.board));
        for (int i = 0; i < visited.size(); i++)
            fillNode(canvas, visited.get(i), Color.rgb(166, 155, 151));
        fillNode(canvas, visited.get(0), ContextCompat.getColor(context, teamColorBright[colors.get(indexCurrent)]));
        if (pause < delay) {
            MyPoint start = visited.get(visited.size() - 1);
            MyPoint end = queue.get(0);
            fillNode(canvas, start, ContextCompat.getColor(context, teamColorBright[colors.get(indexCurrent)]));
            fillRunningNode(canvas, start, end, pause);
        } else if (queue.size() > 0) {
            visited.add(queue.get(0));
            queue.remove(0);
            if (queue.size() != 0)
                pause = 0;
        }
        if (queue.size() == 0) {
            fillNode(canvas, visited.get(visited.size() - 1), ContextCompat.getColor(context, teamColor[colors.get(indexCurrent)]));
            surroundNode(canvas, visited.get(visited.size() - 1), ContextCompat.getColor(context, teamColor[colors.get((indexCurrent + 3) % 6)]));
         } else
            pause++;
        if (pause < delay+1 && queue.size() != 0)
            invalidate();
        else {
            final Handler handler = new Handler();
            handler.postDelayed(() -> ((PlayActivity) context).btnNext.performClick(), 500);
        }
    }

    public void setHelp(List<MyPoint> possibles) {
        this.possibles = possibles;
    }

    private void drawPossibles(Canvas canvas) {
        for (int i = 0; i < possibles.size(); i++)
            fillNode(canvas, possibles.get(i), ContextCompat.getColor(context, R.color.help));
    }

    private void fillRunningNode(Canvas canvas, MyPoint start, MyPoint end, int pause) {
        int xstart = getxScreen(start.x, start.y);
        int ystart = getyScreen(start.y);
        int xend = getxScreen(end.x, end.y);
        int yend = getyScreen(end.y);
        double jumpEffect = Math.sin(pause * Math.PI / (2 * delay));
        int x = (int) (xstart + (xend - xstart) * jumpEffect);
        int y = (int) (ystart + (yend - ystart) * jumpEffect);
        paintBrush.setColor(ContextCompat.getColor(context, teamColorBright[colors.get(indexCurrent)]));
        paintBrush.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, nodeRadius, paintBrush);
    }

    private void fillNode(Canvas canvas, MyPoint point, int color) {
        paintBrush.setStyle(Paint.Style.FILL);
        paintBrush.setColor(color);
        canvas.drawCircle(getxScreen(point.x, point.y), getyScreen(point.y), nodeRadius, paintBrush);
    }

    private void surroundNode(Canvas canvas, MyPoint point, int color) {
        paintBrush.setStyle(Paint.Style.STROKE);
        paintBrush.setColor(color);
        canvas.drawCircle(getxScreen(point.x, point.y), getyScreen(point.y), nodeRadius, paintBrush);
    }

    public void setWay(List<MyPoint> way) {
        visited = new ArrayList<>();
        queue = new ArrayList<>();
        if (way != null && way.size() > 0) {
            visited.add(way.get(0));
            if (way.size() > 1)
                for (int i = 1; i < way.size(); i++)
                    queue.add(way.get(i));
            pause = 0;
        }
    }

    private int getxScreen(double x, double y) {
        return (int) (centerX + (x + 0.5 * y) * nodeGap);
    }

    private int getyScreen(double y) {
        return (int) (centerY - y * nodeGap * Math.sqrt(3) / 2);
    }

    public MyPoint getPointFromScreen(int Xposition, int Yposition) {
        double y = (centerY - Yposition) * 2 / (nodeGap * Math.sqrt(3) );
        double x = (Xposition - centerX + (Yposition - centerY) / Math.sqrt(3) ) / nodeGap;
        return new MyPoint(Math.round((float) x), Math.round((float) y));
    }
}
