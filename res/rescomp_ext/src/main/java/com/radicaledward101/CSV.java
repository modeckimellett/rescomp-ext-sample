package com.radicaledward101.rescomp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import sgdk.rescomp.Resource;
import sgdk.rescomp.resource.Bin;
import sgdk.rescomp.tool.Util;
import sgdk.rescomp.type.Basics.Compression;
import sgdk.tool.FileUtil;

public class CSV extends Resource
{
    final int hc;

    public Bin bin;

    public int width;

    public int height;

    public CSV(String id, String fileIn) throws Exception
    {
        super(id);

        String file = fileIn;
        byte[] csvRawData;

        // CSV file ?
        if (FileUtil.getFileExtension(file, false).equalsIgnoreCase("csv"))
        {
            // get Collision raw data
            csvRawData = FileUtil.load(file, false);
            String csvContent = new String(csvRawData, StandardCharsets.UTF_8);
            String[] csvRows = csvContent.split("\n");
            List<String[]> csvGrid = Arrays.asList(csvRows)
                .stream()
                // git is adding return characters after commit. So we'll delete those
                .map(csvRow -> csvRow.replaceAll("\\r", ""))
                // delete trailing commas
                .map(csvRow -> csvRow.replaceAll(",$", ""))
                // Split to cells by commas
                .map(csvRow -> csvRow.split("\\,"))
                .collect(Collectors.toList());

            List<Byte> csvByteList = new ArrayList<>();

            for (String[] csvRow : csvGrid) {
                for (String csvCell : csvRow) {
                    csvByteList.add(Byte.valueOf(csvCell));
                }
            }

            width = csvGrid.get(0).length;
            height = csvGrid.size();
            bin = (Bin) addInternalResource(new Bin(id + "data", getPrimitiveByteArray(csvByteList), Compression.NONE));
            hc = csvGrid.hashCode();
        }
        else
        {
            hc = -1;
            throw new IllegalArgumentException(
                    file + " does not contain CSV data - must be a csv file");
        }
    }

    @Override
    public int internalHashCode()
    {
        return hc;
    }

    @Override
    public boolean internalEquals(Object obj)
    {
        if (obj instanceof CSV collision)
        {
            return bin.equals(collision.bin);
        }

        return false;
    }

    @Override
    public List<Bin> getInternalBinResources()
    {
        return Arrays.asList(bin);
    }

    @Override
    public int shallowSize()
    {
        return 2 + 4;
    }

    @Override
    public int totalSize()
    {
        return bin.totalSize() + shallowSize();
    }

    @Override
    public void out(ByteArrayOutputStream outB, StringBuilder outS, StringBuilder outH) throws IOException
    {
        // I'm not actually sure if this is necessary but I copied this from somewhere else?
        outB.reset();

        // declare
        Util.decl(outS, outH, "CSV", id, 2, global);
        // csv size info
        outS.append("    dc.w    " + (bin.data.length) + "\n");
        outS.append("    dc.w    " + (width) + " // width\n");
        outS.append("    dc.w    " + (height) + " // height\n");
        // csv data pointer
        outS.append("    dc.l    " + bin.id + "\n");
        outS.append("\n");
    }

    private static byte[] getPrimitiveByteArray(List<Byte> csvByteList) {
        byte[] csvPrimitiveByteArray = new byte[csvByteList.size()];
        for(int i = 0; i < csvByteList.size(); i++) {
            csvPrimitiveByteArray[i] = csvByteList.get(i);
        }
        return csvPrimitiveByteArray;
    }
}