#version 460 core

uniform vec4 color;

out vec4 out_Color;

void main(){
	out_Color = mix(color, vec4(1), smoothstep(0.1,0.05,1.0));
}