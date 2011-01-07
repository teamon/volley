package eu.teamon.volley;

public class FloatVec extends Vec<Float> {
	public FloatVec(float x, float y){
		super(x, y);
	}
	
	public FloatVec add(Vec<Float> that){
		return new FloatVec(this.x + that.x, this.y + that.y);
	}
	
	public FloatVec subtract(Vec<Float> that){
		return new FloatVec(this.x - that.x, this.y - that.y);
	}
	
	public Float distanceTo(Vec<Float> that){
		return new Float(calculateDistanceTo(that));
	}
	
	public Vec<Float> negate(){
		return new FloatVec(-this.x, -this.y);
	}
	
	public Vec<Float> negateX(){
		return new FloatVec(-this.x, this.y);
	}
	
	public Vec<Float> negateY(){
		return new FloatVec(this.x, -this.y);
	}


}