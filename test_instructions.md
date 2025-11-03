# noTrackers App - Comprehensive Testing Guide

## 🧪 **How to Test Your App**

### **Step 1: Build and Install**
1. Build the updated APK in Android Studio
2. Install on your phone
3. Make sure the app appears in your app drawer

### **Step 2: Test Each URL Category**

#### **YouTube URLs** (Should remove `si` parameter)
- **Test**: `https://youtu.be/cjeIz-n-hrE?si=ayWBOz-3ZKS3c_E7`
- **Expected**: `https://youtu.be/cjeIz-n-hrE`
- **How to test**: 
  1. Copy the URL to your browser
  2. Share it from browser
  3. Select "noTrackers" from share menu
  4. Check if `si=` parameter is removed

#### **Spotify URLs** (Should remove `si` and other trackers)
- **Test**: `https://open.spotify.com/track/abc123?si=xyz789&utm_source=twitter`
- **Expected**: `https://open.spotify.com/track/abc123`
- **How to test**: Same as YouTube

#### **Social Media URLs** (Should remove platform-specific trackers)
- **Test**: `https://www.facebook.com/post/123?fbclid=123456&utm_medium=social`
- **Expected**: `https://www.facebook.com/post/123`
- **How to test**: Same as above

#### **E-commerce URLs** (Should remove affiliate trackers)
- **Test**: `https://shop.example.com/product/123?aff_id=123&ref=partner&utm_source=affiliate`
- **Expected**: `https://shop.example.com/product/123`
- **How to test**: Same as above

### **Step 3: Test Edge Cases**

#### **Empty Parameters**
- **Test**: `https://example.com/page?utm_source=&utm_medium=social`
- **Expected**: `https://example.com/page?utm_medium=social` (removes empty utm_source)

#### **Legitimate Parameters (Should NOT be removed)**
- **Test**: `https://example.com/search?q=privacy&page=2&lang=en&id=123`
- **Expected**: `https://example.com/search?q=privacy&page=2&lang=en&id=123` (unchanged)
- **Why**: These are legitimate functional parameters

### **Step 4: Test Different Apps**

Test sharing from:
- ✅ **Chrome browser**
- ✅ **YouTube app**
- ✅ **Spotify app**
- ✅ **Twitter/X app**
- ✅ **Facebook app**
- ✅ **Instagram app**
- ✅ **Any other app that shares URLs**

### **Step 5: Verify Results**

For each test, check:
1. ✅ **App appears in share menu**
2. ✅ **URL opens in noTrackers app**
3. ✅ **Tracking parameters are removed**
4. ✅ **Legitimate parameters are preserved**
5. ✅ **Copy button works**
6. ✅ **Share button works**

## 🐛 **Common Issues to Check**

### **If App Doesn't Appear in Share Menu:**
- Check if app is installed correctly
- Try restarting your phone
- Test with different apps (browser, social media)

### **If Parameters Aren't Removed:**
- Check if the parameter is in our tracking list
- Verify the URL format is correct
- Test with simpler URLs first

### **If Legitimate Parameters Are Removed:**
- This shouldn't happen with our conservative list
- Report any false positives

## 📊 **Test Results Checklist**

- [ ] YouTube `si` parameter removed
- [ ] Spotify `si` parameter removed  
- [ ] Facebook `fbclid` removed
- [ ] Google `gclid` removed
- [ ] UTM parameters removed
- [ ] Affiliate parameters removed
- [ ] Legitimate parameters preserved
- [ ] Copy functionality works
- [ ] Share functionality works
- [ ] App appears in all relevant share menus

## 🎯 **Success Criteria**

Your app is working correctly if:
1. **Tracking parameters are removed** from URLs
2. **Legitimate parameters are preserved**
3. **App appears in share menus** when sharing URLs
4. **Copy and Share buttons work** properly
5. **No legitimate functionality is broken**

## 📝 **Report Issues**

If you find any issues:
1. Note the exact URL that caused the problem
2. Note what was removed vs. what should have been removed
3. Test with the specific URL to reproduce the issue

