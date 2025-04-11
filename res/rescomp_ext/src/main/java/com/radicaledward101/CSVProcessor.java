package com.radicaledward101.rescomp;

import sgdk.rescomp.Compiler;
import sgdk.rescomp.Processor;
import sgdk.rescomp.Resource;
import sgdk.tool.FileUtil;

public class CSVProcessor implements Processor
{
    @Override
    public String getId()
    {
        return "CSV";
    }

    @Override
    public Resource execute(String[] fields) throws Exception
    {
        if (fields.length < 3)
        {
            System.out.println("Wrong CSV definition");
            System.out.println("CSV name file");
            System.out.println("  name       CSV variable name");
            System.out.println("  file       path of the .csv file to convert to MapCollision structure (from LDtk Super Simple Export)");

            return null;
        }

        // get resource id
        final String id = fields[1];
        // get input file
        final String fileIn = FileUtil.adjustPath(Compiler.resDir, fields[2]);

        // add resource file (used for deps generation)
        Compiler.addResourceFile(fileIn);

        return new CSV(id, fileIn);
    }
}