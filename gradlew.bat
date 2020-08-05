package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState = 0;
	int pause = 0;
	float gravity = 0.2f, velocity = 0;
	int manY = 0;
	Random rand;
	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYx = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangle=
	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYx = new ArrayList<Integer>();

	Texture coin;
	int coinCount;
	Texture bomb;
	int bombCount;

	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight() / 2;
		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		rand = new Random();
	}

	public void makecoin() {
		float height = rand.nextFloat() * Gdx.graphics.getHeight();
		coinYx.add((int) height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makebomb() {


		float height1 = rand.nextFloat() * Gdx.graphics.getHeight();
		bombXs.add((int) height1);
		bombYx.add(Gdx.graphics.getWidth());

	}

	@Override
	public void render () {
batch.begin();
batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
if(coinCount<100)
{
	coinCount++;
}
else
{
	coinCount=0;
	makecoin();
}
if(bombCount<250)
{
	bombCount++;
}
else
{
	bombCount=0;
	makebomb();
}
for(int i=0;i<coinXs.size();i++)
{
	batch.draw(coin,coinXs.get(i),coinYx.get(i));
	coinXs.set(i,coinXs.get(i)-4);
}
for(int i=0;i<bombXs.size();i++)
{
	batch.draw(bomb,bombXs.get(i),bombYx.get(i));
	bombXs.set(i,bombXs.get(i)-8);
}
if(Gdx.input.justTouched())
	velocity=-10;
if(pause<8)
{
	pause++;
}
else {
pause=0;

	if (manState < 3) {
		manState++;
	} else {
		manState = 0;
	}
}
velocity+=gravity;
manY-=