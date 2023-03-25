/*
 * Copyright 2014 Miguel Gamboa at CCISEL.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.isel.mpd14.util;

/**
 *
 * @author Miguel Gamboa at CCISEL
 */
public class SneakyUtils {
    
    public static void call(ActionThrowingException a) {
        try {
            a.invoke();
        } catch (Exception ex) {
            throwAsRTException(ex);
        }
    }
    
    public static <T> T call(SupplierThrowingException<T> a) {
        try {
            return a.supply();
        } catch (Exception ex) {
            throwAsRTException(ex);
        }
        throw new AssertionError();
    }
        
    public static void throwAsRTException(Throwable t) {
        SneakyUtils.<RuntimeException>sneakyThrow(t);
    }

    /*
     * Reinier Zwitserloot who, as far as I know, had the first mention of this
     * technique in 2009 on the java posse mailing list.
     * http://www.mail-archive.com/javaposse@googlegroups.com/msg05984.html
     */
    public static <T extends Throwable> void sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
