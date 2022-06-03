#version 460 core

uniform vec4 color;

in vec2 local_coords;
out vec4 out_Color;

void main(){
	float l = length(local_coords);
	if(l > 1.0f){
		discard;
	}
	
	if((l > 0.95f) && (l < 1.0f)){
		out_Color = vec4(1,1,1,1);
	}else{
		out_Color = color;
	}
}