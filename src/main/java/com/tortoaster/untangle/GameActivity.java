package com.tortoaster.untangle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class GameActivity extends Activity implements UntangleView.UntangleListener {
	
	public static final int PREVIOUS_LEVEL = 222, NEXT_LEVEL = 333, LEVEL_FINISHED = 777;
	
	private long seed;
	
	private int moves;
	
	private String level;
	
	private Random random;
	
	private UntangleView untangle;
	
	private TextView levelIndicator;
	private TextView moveIndicator;
	private TextView bestIndicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		untangle = findViewById(R.id.untangle);
		levelIndicator = findViewById(R.id.level);
		moveIndicator = findViewById(R.id.moves);
		bestIndicator = findViewById(R.id.best);
		
		Intent intent = getIntent();
		
		level = intent.getStringExtra("level");
		
		if(level == null) {
			random = new Random();
			seed = intent.getLongExtra("seed", random.nextLong());
			random.setSeed(seed);
		}
		
		load();
		
		untangle.setListener(this);
	}
	
	@SuppressLint("SetTextI18n")
	@Override
	public void move() {
		moves++;
		moveIndicator.setText(Integer.toString(moves));
	}
	
	@Override
	public void win() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		
		if(level == null) {
			level = Long.toString(seed);
		}
		
		int best = preferences.getInt(level, 0);
		
		if(best == 0 || moves < best) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(level, moves);
			editor.apply();
		}
		
		setResult(LEVEL_FINISHED);
		finish();
	}
	
	@SuppressLint("SetTextI18n")
	private void load() {
		int best;
		
		if(level == null) {
			Graph graph;
			
			do {
				graph = Graph.createRandomGraph(seed);
				
				if(!graph.canBePlanar() || graph.isPlanar()) {
					seed = random.nextLong();
				}
			} while(!graph.canBePlanar() || graph.isPlanar());
			
			untangle.setGraph(graph);
			levelIndicator.setText(R.string.random);
			
			best = getPreferences(MODE_PRIVATE).getInt(Long.toString(seed), 0);
		} else {
			float[][] vertices;
			int[][] edges;
			
			try {
				InputStream in = getAssets().open("levels" + File.separator + level);
				
				vertices = new float[Integer.parseInt(getLine(in))][2];
				
				for(int i = 0; i < vertices.length; i++) {
					String[] vertex = getLine(in).split(" ");
					
					vertices[i][0] = Float.parseFloat(vertex[0]);
					vertices[i][1] = Float.parseFloat(vertex[1]);
				}
				
				edges = new int[Integer.parseInt(getLine(in))][2];
				
				for(int i = 0; i < edges.length; i++) {
					String[] edge = getLine(in).split(" ");
					
					edges[i][0] = Integer.parseInt(edge[0]);
					edges[i][1] = Integer.parseInt(edge[1]);
				}
				
				in.close();
				
				untangle.setGraph(Graph.createGraph(vertices, edges));
				levelIndicator.setText(level);
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			best = getPreferences(MODE_PRIVATE).getInt(level, 0);
		}
		
		moves = 0;
		
		moveIndicator.setText(Integer.toString(moves));
		bestIndicator.setText(best > 0 ? Integer.toString(best) : "-");
	}
	
	private static String getLine(InputStream in) throws IOException {
		StringBuilder line = new StringBuilder();
		char c;
		
		while((c = (char) in.read()) != '\n') {
			line.append(c);
		}
		
		return line.toString().trim();
	}
	
	public void previous(View view) {
		setResult(PREVIOUS_LEVEL);
		finish();
	}
	
	public void retry(View view) {
		load();
	}
	
	public void next(View view) {
		setResult(NEXT_LEVEL);
		finish();
	}
}
