#version 460 core

//
// Ce shader met à jour la grille
//

layout(local_size_x = 8, local_size_y = 8, local_size_z = 1) in;

//layout(binding = 0, rgba8) uniform restrict readonly image2D gridOld;
layout(binding = 1, rgba8) uniform restrict image2D gridNew;

void main(void){

    ivec2 coords = ivec2(gl_GlobalInvocationID.xy);

    //vec4 c = imageLoad(gridOld, coords);

    //imageStore(gridNew, coords, c);
    imageStore(gridNew, coords, vec4(1.0,0.0,0.0,1.0));
}