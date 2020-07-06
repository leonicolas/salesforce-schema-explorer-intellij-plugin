package com.schemaexplorer.view.node;

import javax.swing.*;
import java.awt.*;

public interface IconNode {

    Rectangle getNodeBounds();

    void setNodeBounds(Rectangle nodeBounds);

    ImageIcon getIcon();

    void setIcon(ImageIcon icon);

    void setRow(int row);

    int getRow();
}
