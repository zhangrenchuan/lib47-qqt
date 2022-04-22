package cn.kizzzy.vfs.handler;

import cn.kizzzy.io.FullyReader;
import cn.kizzzy.io.FullyWriter;
import cn.kizzzy.qqt.MapElemProp;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class MapElemPropHandler implements IFileHandler<MapElemProp> {
    
    @Override
    public MapElemProp load(IPackage vfs, String path, FullyReader reader, long size) throws Exception {
        MapElemProp prop = new MapElemProp();
        prop.version = reader.readIntEx();
        prop.elements = new MapElemProp.Element[reader.readIntEx()];
        for (int i = 0, m = prop.elements.length; i < m; ++i) {
            MapElemProp.Element element = new MapElemProp.Element();
            element.id = reader.readIntEx();
            element.width = reader.readShortEx();
            element.height = reader.readShortEx();
            element.x = reader.readShortEx();
            element.y = reader.readShortEx();
            element.life = reader.readIntEx();
            element.level = reader.readIntEx();
            element.special = reader.readIntEx();
            element.attrs = new int[element.width * element.height];
            for (int j = 0, n = element.attrs.length; j < n; ++j) {
                element.attrs[j] = reader.readIntEx();
            }
            
            prop.elements[i] = element;
        }
        return prop;
    }
    
    @Override
    public boolean save(IPackage vfs, String path, FullyWriter writer, MapElemProp prop) throws Exception {
        writer.writeIntEx(prop.version);
        writer.writeIntEx(prop.elements.length);
        for (MapElemProp.Element element : prop.elements) {
            writer.writeIntEx(element.id);
            writer.writeShort(element.width);
            writer.writeShort(element.height);
            writer.writeShort(element.x);
            writer.writeShort(element.y);
            writer.writeIntEx(element.life);
            writer.writeIntEx(element.level);
            writer.writeIntEx(element.special);
            for (int attr : element.attrs) {
                writer.writeIntEx(attr);
            }
        }
        
        return true;
    }
}
