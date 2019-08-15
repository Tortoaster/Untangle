package com.tortoaster.untangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Graph {
	
	private List<Vertex> vertices;
	private List<Edge> edges;
	
	private Graph(List<Vertex> vertices, List<Edge> edges) {
		this.vertices = vertices;
		this.edges = edges;
	}
	
	public boolean isPlanar() {
		boolean planar = true;
		
		for(Vertex v : vertices) {
			v.setValid(true);
		}
		
		for(Edge e : edges) {
			e.setValid(true);
		}
		
		for(int i = 0; i < edges.size() - 1; i++) {
			Edge current = edges.get(i);
			
			for(int n = i + 1; n < edges.size(); n++) {
				Edge subject = edges.get(n);
				if(current.intersects(subject)) {
					
					current.setValid(false);
					subject.setValid(false);
					
					planar = false;
				}
			}
		}
		
		return planar;
	}
	
	// TODO: implement
	public boolean canBePlanar() {
		return true;
	}
	
	public static Graph createRandomGraph(long seed) {
		List<Vertex> vertices = new ArrayList<>();
		List<Edge> edges = new ArrayList<>();
		
		Random random = new Random(seed);
		
		HaltonSequence h1 = new HaltonSequence(3, random.nextInt(256));
		HaltonSequence h2 = new HaltonSequence(2, random.nextInt(256));
		
		int vertexAmount = random.nextInt(3) + 8;
		
		for(int i = 0; i < vertexAmount; i++)
			vertices.add(new Vertex(((6 * h1.next() + 1) / 8), ((6 * h2.next() + 1) / 8)));
		
		for(Vertex v1 : vertices) {
			int edgeCount = random.nextInt(2) + 2 - v1.getDegree();
			
			for(int i = 0; i < edgeCount; i++) {
				Vertex v2;
				do {
					v2 = vertices.get(random.nextInt(vertices.size()));
				} while(v1 == v2);
				
				Edge e = new Edge(v1, v2);
				
				if(!edges.contains(e)) {
					edges.add(e);
					v1.addConnection();
					v2.addConnection();
				} else if(v1.getDegree() < 2) i--;
			}
		}
		
		return new Graph(vertices, edges);
	}
	
	public static Graph createGraph(float[][] v, int[][] e) {
		List<Vertex> vertices = new ArrayList<>();
		List<Edge> edges = new ArrayList<>();
		
		for(float[] vertex : v) {
			vertices.add(new Vertex(vertex[0], vertex[1]));
		}
		
		for(int[] edge : e) {
			edges.add(new Edge(vertices.get(edge[0]), vertices.get(edge[1])));
		}
		
		return new Graph(vertices, edges);
	}
	
	public Vertex getVertexAt(float x, float y, float radius) {
		for(Vertex v : vertices) {
			double distance = Math.sqrt(Math.pow(v.getX() - x, 2) + Math.pow(v.getY() - y, 2));
			if(distance < radius) return v;
		}
		
		return null;
	}
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
}
