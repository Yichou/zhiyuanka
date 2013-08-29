package com.zhiyuanka.app.bean;

public final class Mbti {
	private int P;
	private int E;
	private int S;
	private int F;
	

	public Mbti(int p, int e, int s, int f) {
		set(p, e, s, f);
	}
	
	public void set(int p, int e, int s, int f) {
		P = p;
		E = e;
		S = s;
		F = f;
	}

	public int getP() {
		return P;
	}

	public int getE() {
		return E;
	}

	public int getS() {
		return S;
	}

	public int getF() {
		return F;
	}

	public int getJ() {
		return 100 - P;
	}

	public int getI() {
		return 100 - E;
	}

	public int getN() {
		return 100 - S;
	}

	public int getT() {
		return 100 - F;
	}

	@Override
	public String toString() {
		return "[" + "P=" + P 
		 	+ ",E=" + E 
		 	+ ",S=" + S 
		 	+ ",F=" + F 
		 	+"]";
	}
}
