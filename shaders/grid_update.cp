#version 460 core

//
// Ce shader met à jour la grille
//

layout(local_size_x = 8, local_size_y = 8, local_size_z = 1) in;

layout(binding = 0, rgba8) uniform restrict image2D gridOld;
layout(binding = 1, rgba8) uniform restrict image2D gridNew;

uniform ivec2 mouseCoords;
uniform float radius;
uniform int lmbState;
uniform int rmbState;
uniform int updateRound;

bool isFree(ivec2 coords){
    return coords.y > 0 && imageLoad(gridOld, coords).a == 0;
}

bool willBeFree(ivec2 coords){
    return coords.y > 0 && imageLoad(gridNew, coords).a == 0;
}

const ivec2 DOWN = ivec2(0, 1);
const ivec2 UP = ivec2(0, -1);

void main(void){

    ivec2 coords = ivec2(gl_GlobalInvocationID.xy);
    ivec4 c;
    
    if((rmbState > 0 || lmbState > 0) && length(coords - mouseCoords) < radius){
        if(lmbState > 0){
            c = ivec4(128, 128, 0, 1); //fill
        }
        if(rmbState > 0){
            c = ivec4(128, 128, 0, 0); //erase
        }
    }else{
        c = ivec4(imageLoad(gridOld, coords) * 255.0f);
    }


    ivec2 velocity = c.xy - 128;
    int type = c.a;

    if(type == 0){
        return;//skip if not a particle
    }

    
    if(updateRound == 0){
        //update the velocity
        if(isFree(coords + DOWN));
    }

    //accelerate
    velocity = ivec2(0, +1);

    ivec2 nextPos = coords + velocity;

    //isFree(nextPos);

    //willBeFree(coords);

    if(updateRound == 0){
        c = ivec4(velocity + 128, 0, type);
        imageStore(gridNew, nextPos, c / 255.0f);
        imageStore(gridOld, nextPos, ivec4(128, 128, 0, 0) / 255.0f);
    }else if(updateRound == 1){
        velocity == ivec2(0, 0);
        c = ivec4(velocity + 128, 0, type);
        imageStore(gridNew, coords, c / 255.0f);
    }


}