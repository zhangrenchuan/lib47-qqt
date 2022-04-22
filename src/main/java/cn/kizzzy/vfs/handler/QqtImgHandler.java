package cn.kizzzy.vfs.handler;

import cn.kizzzy.helper.LogHelper;
import cn.kizzzy.image.sizer.QqtSizerHelper;
import cn.kizzzy.io.FullyReader;
import cn.kizzzy.io.FullyWriter;
import cn.kizzzy.io.SeekType;
import cn.kizzzy.qqt.QqtImg;
import cn.kizzzy.qqt.QqtImgItem;
import cn.kizzzy.vfs.IFileHandler;
import cn.kizzzy.vfs.IPackage;

public class QqtImgHandler implements IFileHandler<QqtImg> {
    
    @Override
    public QqtImg load(IPackage vfs, String path, FullyReader reader, long size) throws Exception {
        try {
            QqtImg img = new QqtImg();
            img.magic01 = reader.readIntEx();
            img.magic02 = reader.readIntEx();
            img.major = reader.readShortEx();
            img.minor = reader.readShortEx();
            img.headerSize = reader.readIntEx();
            img.count = reader.readIntEx();
            img.planes = reader.readIntEx();
            img.offsetX = reader.readIntEx();
            img.offsetY = reader.readIntEx();
            img.maxWidth = reader.readIntEx();
            img.maxHeight = reader.readIntEx();
            
            img.items = new QqtImgItem[img.count];
            for (int i = 0; i < img.items.length; ++i) {
                QqtImgItem item = new QqtImgItem();
                item.index = i;
                item.file = img;
                
                item.reserved01 = reader.readIntEx();
                item.offsetX = reader.readIntEx();
                item.offsetY = reader.readIntEx();
                item.reserved04 = reader.readIntEx();
                if (item.reserved04 != 0) {
                    item.width = reader.readIntEx();
                    item.height = reader.readIntEx();
                    item.reserved07 = reader.readIntEx();
                }
                
                item.valid = 0 < item.width && item.width < 4096
                    && 0 < item.height && item.height < 4096;
                
                item.offset = reader.position();
                item.size = QqtSizerHelper.calc(img.major, item.width, item.height);
                
                reader.seek(item.size, SeekType.CURRENT);
                
                if (img.major == 0) {
                    item.offset_alpha = reader.position();
                    item.size_alpha = item.width * item.height;
                    
                    reader.seek(item.size_alpha, SeekType.CURRENT);
                }
                
                img.items[i] = item;
            }
            
            return img;
        } catch (Exception e) {
            LogHelper.error("load qqt img error: " + path, e);
            return null;
        }
    }
    
    @Override
    public boolean save(IPackage vfs, String path, FullyWriter writer, QqtImg img) throws Exception {
        writer.writeIntEx(img.magic01);
        writer.writeIntEx(img.magic02);
        writer.writeShortEx(img.major);
        writer.writeShortEx(img.minor);
        writer.writeIntEx(img.headerSize);
        writer.writeIntEx(img.count);
        writer.writeIntEx(img.planes);
        writer.writeIntEx(img.offsetX);
        writer.writeIntEx(img.offsetY);
        writer.writeIntEx(img.maxWidth);
        writer.writeIntEx(img.maxHeight);
        
        for (QqtImgItem item : img.items) {
            writer.writeIntEx(item.reserved01);
            writer.writeIntEx(item.offsetX);
            writer.writeIntEx(item.offsetY);
            writer.writeIntEx(item.reserved04);
            if (item.reserved04 != 0) {
                writer.writeIntEx(item.width);
                writer.writeIntEx(item.height);
                writer.writeIntEx(item.reserved07);
            }
            
            // todo write image data
            
            if (img.major == 0) {
                // todo write image alpha data
            }
        }
        
        return true;
    }
}
