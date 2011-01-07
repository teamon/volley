package eu.teamon.volley;

public class IntVec extends Vec<Integer> {
	public IntVec(int x, int y){
		super(x, y);
	}
	
	public IntVec add(Vec<Integer> that){
		return new IntVec(this.x + that.x, this.y + that.y);
	}	
	
	public IntVec subtract(Vec<Integer> that){
		return new IntVec(this.x - that.x, this.y - that.y);
	}
	
	public Integer distanceTo(Vec<Integer> that){
		return new Integer((int)calculateDistanceTo(that));
	}
	
	public Vec<Integer> negate(){
		return new IntVec(-this.x, -this.y);
	}
	
	public Vec<Integer> negateX(){
		return new IntVec(-this.x, this.y);
	}
	
	public Vec<Integer> negateY(){
		return new IntVec(this.x, -this.y);
	}

}