/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.apache.ofbiz.base.util;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
/**
 * File Utilities
 *
 */
public final class FileUtil {

    public static final String module = FileUtil.class.getName();

    private FileUtil () {}

    private static class SearchTextFilesFilter implements FilenameFilter {
        String fileExtension;
        Set<String> stringsToFindInFile = new HashSet<>();
        Set<String> stringsToFindInPath = new HashSet<>();

        public SearchTextFilesFilter(String fileExtension, Set<String> stringsToFindInPath, Set<String> stringsToFindInFile) {
            this.fileExtension = fileExtension;
            if (stringsToFindInPath != null) {
                this.stringsToFindInPath.addAll(stringsToFindInPath);
            }
            if (stringsToFindInFile != null) {
                this.stringsToFindInFile.addAll(stringsToFindInFile);
            }
        }
        @Override
        public boolean accept(File dir, String name) {
            File file = new File(dir, name);
            if (file.getName().startsWith(".")) {
                return false;
            }
            if (file.isDirectory()) {
                return true;
            }

            boolean hasAllPathStrings = true;
            String fullPath = dir.getPath().replace('\\', '/');
            for (String pathString: stringsToFindInPath) {
                if (fullPath.indexOf(pathString) < 0) {
                    hasAllPathStrings = false;
                    break;
                }
            }
            if (hasAllPathStrings && name.endsWith("." + fileExtension)) {
                if (stringsToFindInFile.size() == 0) {
                    return true;
                }
                StringBuffer xmlFileBuffer = null;
                try {
                    xmlFileBuffer = FileUtil.readTextFile(file, true);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                if (xmlFileBuffer != null) {
                    boolean hasAllStrings = true;
                    for (String stringToFile: stringsToFindInFile) {
                        if (xmlFileBuffer.indexOf(stringToFile) < 0) {
                            hasAllStrings = false;
                            break;
                        }
                    }
                    return hasAllStrings;
                }
            } else {
                return false;
            }
            return false;
        }
    }

    public static StringBuffer readTextFile(File file, boolean newline) throws FileNotFoundException, IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        StringBuffer buf = new StringBuffer();
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), UtilIO
                        .getUtf8()));) {

            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
                if (newline) {
                    buf.append(System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buf;
    }

    public static void searchFiles(List<VirtualFile> fileList, File path, FilenameFilter filter, boolean includeSubfolders) throws IOException {
        // Get filtered files in the current path
        File[] files = path.listFiles(filter);
        if (files == null) {
            return;
        }

        // Process each filtered entry
        for (int i = 0; i < files.length; i++) {
            // recurse if the entry is a directory
            if (files[i].isDirectory() && includeSubfolders && !files[i].getName().startsWith(".")) {
                searchFiles(fileList, files[i], filter, true);
            } else {
                // add the filtered file to the list
                VirtualFile virtualFile = VfsUtil.findFileByIoFile(files[i], false);
                fileList.add(virtualFile);
            }
        }
    }

    public static List<VirtualFile> findFiles(String fileExt, String basePath, String partialPath, String stringToFind) throws IOException {
        if (basePath == null) {
            basePath = System.getProperty("ofbiz.home");
        }

        Set<String> stringsToFindInPath = new HashSet<>();
        Set<String> stringsToFindInFile = new HashSet<>();

        if (partialPath != null) {
            stringsToFindInPath.add(partialPath);
        }
        if (stringToFind != null) {
            stringsToFindInFile.add(stringToFind);
        }

        List<VirtualFile> fileList = new LinkedList<>();
        FileUtil.searchFiles(fileList, new File(basePath), new SearchTextFilesFilter(fileExt, stringsToFindInPath, stringsToFindInFile), true);

        return fileList;
    }

    public static List<VirtualFile> findXmlFiles(String basePath, String partialPath, String rootElementName, String xsdOrDtdName) throws IOException {
        if (basePath == null) {
            basePath = System.getProperty("ofbiz.home");
        }

        Set<String> stringsToFindInPath = new HashSet<>();
        Set<String> stringsToFindInFile = new HashSet<>();

        if (partialPath != null) {
            stringsToFindInPath.add(partialPath);
        }
        if (rootElementName != null) {
            stringsToFindInFile.add("<" + rootElementName + " ");
        }
        if (xsdOrDtdName != null) {
            stringsToFindInFile.add(xsdOrDtdName);
        }

        List<VirtualFile> fileList = new LinkedList<>();
        FileUtil.searchFiles(fileList, new File(basePath), new SearchTextFilesFilter("xml", stringsToFindInPath, stringsToFindInFile), true);
        return fileList;
    }

    /**
     *
     *
     * Search for the specified <code>searchString</code> in the given
     * {@link Reader}.
     *
     * @param reader A Reader in which the String will be searched.
     * @param searchString The String to search for
     * @return <code>TRUE</code> if the <code>searchString</code> is found;
     *         <code>FALSE</code> otherwise.
     * @throws IOException
     */
    public static boolean containsString(Reader reader, final String searchString) throws IOException {
        char[] buffer = new char[1024];
        int numCharsRead;
        int count = 0;
        while((numCharsRead = reader.read(buffer)) > 0) {
            for (int c = 0; c < numCharsRead; ++c) {
                if (buffer[c] == searchString.charAt(count)) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == searchString.length()) {
                    return true;
                }
            }
        }
        return false;
    }

}
