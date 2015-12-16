#import "DeviceInfoModel.h"

/**
 * Created by max on 06.02.14.
 */

@interface LoginRequest : NSObject {
  NSString * login;
  NSString * password;
  DeviceInfoModel * deviceInfoModel;
}

@property(nonatomic, retain) NSString * login;
@property(nonatomic, retain) NSString * password;
@property(nonatomic, retain) DeviceInfoModel * deviceInfoModel;
- (void) setLogin:(NSString *)login;
- (void) setPassword:(NSString *)password;
- (void) setDeviceInfoModel:(DeviceInfoModel *)deviceInfoModel;
@end
