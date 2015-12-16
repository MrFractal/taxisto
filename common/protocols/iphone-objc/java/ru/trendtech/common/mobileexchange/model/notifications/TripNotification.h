
@interface TripNotification : NSObject {
  long missionId;
  BOOL begin;
}

@property(nonatomic) long missionId;
@property(nonatomic) BOOL begin;
- (void) init;
- (void) setMissionId:(long)missionId;
- (void) setBegin:(BOOL)begin;
@end
