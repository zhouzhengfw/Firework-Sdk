package com.loopnow.firework.fwsdk.preference.sharedpreferences;

import static com.loopnow.firework.fwsdk.preference.sharedpreferences.io.IoUtils.close;

import android.content.Context;
import android.util.Log;

import com.loopnow.firework.fwsdk.preference.sharedpreferences.io.XmlUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author neighbWang
 */
class SharedPreferencesManager {

    private static final String TAG = "SharedPreferenceManager";
    private static final int S_IRWXU = 00700;
    private static final int S_IRWXG = 00070;
    private static final int S_IRWXO = 00007;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final File mSpFile;
    private final File mTempFile;

    SharedPreferencesManager(Context context, String name) {
        this.mSpFile = new File(getPreferencesDir(context), name + ".xml");
        this.mTempFile = new File(mSpFile.getPath() + ".tmp");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    boolean write(final Map<String, Object> map) {
        if (!mTempFile.exists()) {
            try {
                mTempFile.createNewFile();
            } catch (final IOException e) {
                prepare();
                try {
                    mTempFile.createNewFile();
                } catch (IOException ex) {
                    Log.e(TAG, "Couldn't create tempfile" + mTempFile, ex);
                }
                return false;
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mTempFile);
        } catch (final FileNotFoundException e) {
            Log.e(TAG, "Couldn't write SharedPreferences file " + mTempFile, e);
        }
        if (fos != null) {
            final Lock lock = readWriteLock.writeLock();
            try {
                lock.lock();
                XmlUtils.writeMapXml(map, fos);
                sync(fos);
                if (mSpFile.exists()) {
                    mSpFile.delete();
                }
                return mTempFile.renameTo(mSpFile);
            } catch (final Exception e) {
                Log.e(TAG, "write message failed : " + e.getMessage());
                return false;
            } finally {
                lock.unlock();
                close(fos);
            }

        }
        return false;
    }

    Map<String, Object> read() {
        if (!mSpFile.exists()) {
            return null;
        }
        prepare();
        if (mSpFile.canRead()) {
            BufferedInputStream str = null;
            final Lock lock = readWriteLock.readLock();
            try {
                lock.lock();
                str = new BufferedInputStream(new FileInputStream(this.mSpFile), 16 * 1024);
                return XmlUtils.readMapXml(str);
            } catch (Exception e) {
                return null;
            } finally {
                lock.unlock();
                close(str);
            }
        }
        return null;
    }

    private void prepare() {
        File parent = mSpFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (parent.exists() && (!parent.canRead() || !parent.canWrite())) {
            setPermissions(parent.getPath());
        }
    }

    private static void setPermissions(String path) {
        try {
            Runtime.getRuntime().exec("chmod " + Integer.toOctalString(S_IRWXU | S_IRWXG | S_IRWXO) + " " + path);
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
    }

    private static void sync(FileOutputStream stream) {
        try {
            if (stream != null) {
                stream.getFD().sync();
            }
        } catch (IOException ignored) {
        }
    }

    private static File getPreferencesDir(Context context) {
        final File files = context.getFilesDir();
        final File data = null != files ? files.getParentFile() : getDataDirFile(context);
        return new File(data, "shared_prefs");
    }

    private static File getDataDirFile(Context context) {
        final String dataDir = context.getApplicationInfo().dataDir;
        return new File(null != dataDir ? dataDir : "/data/data/" + context.getPackageName());
    }

}
