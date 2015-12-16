#import "DeviceInfoModel.h"

/**
 * Created by max on 09.04.2014.
 */

@interface LogoutRequest : NSObject {
  long requesterId;
  BOOL force;
  DeviceInfoModel * deviceInfo;
}

@property(nonatomic) long requesterId;
@property(nonatomic) BOOL force;
@property(nonatomic, retain) DeviceInfoModel * deviceInfo;
- (void) setRequesterId:(long)requesterId;
- (void) setForce:(BOOL)force;
- (void) setDeviceInfo:(DeviceInfoModel *)deviceInfo;
@end
