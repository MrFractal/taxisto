
/**
 * Created by ivanenok on 4/4/2014.
 */

@interface UpdateDeviceInfoRequest : NSObject {
  long requesterId;
  DeviceInfoModel * deviceInfoModel;
}

@property(nonatomic) long requesterId;
@property(nonatomic, retain) DeviceInfoModel * deviceInfoModel;
- (void) setRequesterId:(long)requesterId;
- (void) setDeviceInfoModel:(DeviceInfoModel *)deviceInfoModel;
@end
