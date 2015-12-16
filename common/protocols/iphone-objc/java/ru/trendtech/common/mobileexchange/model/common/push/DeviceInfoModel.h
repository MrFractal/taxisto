
/**
 * Created by max on 09.04.2014.
 */

@interface DeviceInfoModel : NSObject {
  NSString * token;
  int deviceType;
}

@property(nonatomic, retain) NSString * token;
@property(nonatomic) int deviceType;
- (void) setToken:(NSString *)token;
- (void) setDeviceType:(int)deviceType;
@end
