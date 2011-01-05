package eu.teamon.volley;

public class IntVec extends Vec<Integer> {
	public IntVec(int x, int y){
		super(x, y);
	}
	public IntVec add(Vec<Integer> that){
		return new IntVec(this.x + that.x, this.y + that.y);
	}
}