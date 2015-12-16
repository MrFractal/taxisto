
@interface StartTripResponse : NSObject {
  BOOL started;
}

@property(nonatomic) BOOL started;
- (void) init;
- (void) setStarted:(BOOL)started;
@end
