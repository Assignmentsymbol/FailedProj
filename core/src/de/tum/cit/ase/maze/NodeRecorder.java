package de.tum.cit.ase.maze;

import com.badlogic.gdx.math.Vector2;

public class NodeRecorder {
    Vector2 nodeP;
    float value;

    public NodeRecorder(Vector2 nodeP, float value) {
        this.nodeP = nodeP;
        this.value = value;
    }
}
