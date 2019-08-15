package com.tortoaster.untangle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
	
	private long seed;
	
	private int numberOfLevels, currentLevel;
	
	private String[] levels;
	
	private Random random;
	
	private void startLevel() {
		if(currentLevel == numberOfLevels) {
			Intent intent = new Intent(this, GameActivity.class);
			intent.putExtra("seed", seed);
			startActivityForResult(intent, GameActivity.LEVEL_FINISHED);
		} else {
			Intent intent = new Intent(this, GameActivity.class);
			intent.putExtra("level", levels[currentLevel]);
			startActivityForResult(intent, GameActivity.LEVEL_FINISHED);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		random = new Random();
		
		try {
			levels = getAssets().list("levels");
			
			if(levels != null) {
				numberOfLevels = levels.length;
				levels = Arrays.copyOf(levels, numberOfLevels + 1);
				levels[numberOfLevels] = "?";
				
				GridView levelList = findViewById(R.id.levels);
				ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, levels);
				
				levelList.setAdapter(adapter);
				levelList.setOnItemClickListener(this);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onItemClick(AdapterView parent, View v, int position, long id) {
		currentLevel = numberOfLevels;
		String level = ((TextView) v).getText().toString();
		
		for(int i = 0; i < numberOfLevels; i++) {
			if(level.equals(levels[i])) {
				currentLevel = i;
				break;
			}
		}
		
		seed = random.nextLong();
		
		startLevel();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(resultCode) {
			case GameActivity.LEVEL_FINISHED:
			case GameActivity.NEXT_LEVEL:
				if(currentLevel < numberOfLevels) currentLevel++;
				seed++;
				
				startLevel();
				
				break;
			case GameActivity.PREVIOUS_LEVEL:
				if(currentLevel == 0) break;
				if(currentLevel > 0 && currentLevel != numberOfLevels) currentLevel--;
				seed--;
				
				startLevel();
				
				break;
		}
	}
}
