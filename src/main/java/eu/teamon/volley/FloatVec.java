package eu.teamon.volley;

public class FloatVec extends Vec<Float> {
	public FloatVec(float x, float y){
		super(x, y);
	}
	public FloatVec add(Vec<Float> that){
		return new FloatVec(this.x + that.x, this.y + that.y);
	}
}