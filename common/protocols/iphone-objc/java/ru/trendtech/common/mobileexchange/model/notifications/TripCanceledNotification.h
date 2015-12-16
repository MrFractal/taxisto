
/**
 * Created by ivanenok on 4/14/2014.
 */

@interface TripCanceledNotification : NSObject {
  long missionId;
  int reason;
  NSString * reasonMsg;
}

@property(nonatomic) long missionId;
@property(nonatomic) int reason;
@property(nonatomic, retain) NSString * reasonMsg;
- (void) setMissionId:(long)missionId;
- (void) setReason:(int)reason;
- (void) setReasonMsg:(NSString *)reasonMsg;
@end
