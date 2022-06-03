#version 460 core

in vec2 position;
out vec2 local_coords;

uniform mat4 transform;

void main(){
	local_coords = position;
	gl_Position = transform * vec4(position, 0.0, 1.0);
}